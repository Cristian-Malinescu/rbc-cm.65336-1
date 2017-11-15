/*
 * Copyright 20017 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.rbc.cm.j65336n1.business;

import static com.rbc.cm.j65336n1.api.model.State.EXECUTED;
import static com.rbc.cm.j65336n1.api.model.State.OPEN;
import static java.util.Optional.of;
import static reactor.core.publisher.Flux.fromStream;
import static reactor.core.publisher.Mono.just;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rbc.cm.j65336n1.api.model.Direction;
import com.rbc.cm.j65336n1.api.model.Order;
import com.rbc.cm.j65336n1.api.model.RIC;
import com.rbc.cm.j65336n1.api.model.State;
import com.rbc.cm.j65336n1.api.model.User;
import com.rbc.cm.j65336n1.middleware.DataDepot;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@Component
@ThreadSafe
public class RulesEngine {

  /*
   * 
   **/
  public Flux<Order> all() {
    final Optional<Collection<Order>> data = _depo.get();
    
    return data.isPresent() ? fromStream(data.get().stream())
                            : Flux.empty();
  }
  
  /*
   * 
   **/
  public Flux<Order> all_4_status(final Optional<State> s) {
    return all().filter(o -> OPEN.equals(o.state()));
  }

  public Flux<Order> open() { return all_4_status(of(OPEN)); }
  
  public Flux<Order> executed() { return all_4_status(of(EXECUTED)); }
  
  public Mono<List<Order>> time_series_mn() {
    return open().collectSortedList((a, b) -> {
      return a.timeStamp().compareTo(b.timeStamp());
    });
  }

  /*
   * 
   **/
  public Flux<Order> time_series_fx() { return time_series_mn().flatMapIterable(l -> l); }

  /*
   * 
   **/
  public Flux<Order> matches_fx(final Optional<Order> o) {
    return time_series_fx().filter(p -> o.get().match(p));
  }

  /*
   * 
   **/
  public Mono<List<Order>> matches(final Optional<Order> o) {
    return matches_fx(o).collectSortedList((a, b) -> {
      return a.timeStamp().compareTo(b.timeStamp());
    });
  }

  /*
   * 
   **/
  public Mono<Optional<Order>> execute(final Optional<Order> o) {
    return matches(o).flatMap(l -> {
      final TreeSet<Order> matches = new TreeSet<>(Order.ByPriceMatcher);
      
      l.forEach(n -> matches.add(n));

      if (1 == matches.size()) execute_same_price(o, matches);
        else switch (o.get().direction()) {
               case SELL: execute_at_highest_price(o, matches);
                    break;
               case BUY: execute_at_lowest_price(o, matches);
                    break;
               default: throw new IllegalStateException();
      }

      return just(o);
    });
  }

  /*
   * 
   **/
  private void execute_same_price(final Optional<Order> o, final TreeSet<Order> matches) {
    final Order a = o.get(),
                b = matches.first();

    _lg.debug("Executing [{}] at SAME price [{}] ", a.direction(), b.price());
    
    a.state(EXECUTED);
    b.state(EXECUTED);
  }
  
  /*
   * 
   **/
  private void execute_at_lowest_price(final Optional<Order> o, final TreeSet<Order> matches) {
    final Order a = o.get(),
                b = matches.last();

    _lg.debug("Executing [{}] at LOW price [{}] ", a.direction(), b.price());

    a.state(EXECUTED);
    b.state(EXECUTED);
  }
  
  /*
   * 
   **/
  private void execute_at_highest_price(final Optional<Order> o, final TreeSet<Order> matches) {
    final Order a = o.get(),
                b = matches.first();

    _lg.debug("Executing [{}] at HIGH price [{}] ", a.direction(), b.price());
    
    a.state(EXECUTED);
    b.state(EXECUTED);
  }

  @PostConstruct
  void init() {
    assert null != _depo;
    _lg.info("This [{}] attached to [{}]", toString().substring(toString().lastIndexOf('.') + 1),
                                           _depo.toString().substring(_depo.toString().lastIndexOf('.') + 1));
  }

  @Autowired
  private DataDepot<?> _depo;

  public RulesEngine() {}

  final private Logger _lg = LoggerFactory.getLogger(getClass());
    
  /*
   * 
   **/
  public Optional<Filter> build(final Optional<Order> criteria) {
    assert null != criteria;
    Optional<Filter> strategy = of(new All());
    
    if (criteria.isPresent()) {
      final Order val = criteria.get();
    
      final RIC ric = val.ric();
      final Direction drctn = val.direction();
      final User usr = val.user();
      
      if (null != ric) {
        if (null != usr && null != drctn) strategy = of(new All());

        if (null == usr  && null == drctn) strategy = of(new AverageExecutionPrice(criteria));
        
        if (null != usr && null == drctn) strategy = of(new ExecutedQuantity(criteria));
        
        if (null != drctn && null == usr) strategy = of(new OpenInterest(criteria));
      }
    }
    
    return strategy;
  }

  /*
   * 
   **/
  public interface Filter { Flux<Order> orders(); }

  /*
   * 
   **/
  private abstract class FilterTemplate implements Filter {
    @Override
    public Flux<Order> orders() { return _criteria.isPresent() ? all() : Flux.empty(); }
    
    final protected Optional<Order> _criteria;

    protected FilterTemplate() { _criteria = Optional.empty(); }
    
    protected FilterTemplate(final Optional<Order> criteria) { _criteria = criteria; }
  }
  
  /*
   * 
   **/
  private final class OpenInterest extends FilterTemplate {
    @Override
    public Flux<Order> orders() {
      return all();
    }

    OpenInterest(final Optional<Order> criteria) { super(criteria); }
  }
  
  /*
   * 
   **/
  private final class AverageExecutionPrice extends FilterTemplate {
    @Override
    public Flux<Order> orders() {
      return all();
    }

    AverageExecutionPrice(final Optional<Order> criteria) { super(criteria); }
  }

  /*
   * 
   **/
  private final class ExecutedQuantity extends FilterTemplate {
    @Override
    public Flux<Order> orders() {
      return all();
    }

    ExecutedQuantity(final Optional<Order> criteria) { super(criteria); }
  }

  /*
   * 
   **/
  private final class All extends FilterTemplate { All() { super(of(new Order())); } }
  
  /*
   * 
   **/
  private final class Empty extends FilterTemplate { Empty() {/**/} }
}
