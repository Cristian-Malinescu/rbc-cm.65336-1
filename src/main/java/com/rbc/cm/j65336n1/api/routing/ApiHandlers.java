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
package com.rbc.cm.j65336n1.api.routing;

import static com.rbc.cm.j65336n1.api.model.Schemas.Entity.Meta.Fields.ID;
import static com.rbc.cm.j65336n1.api.model.Schemas.Order.Meta.Fields.DIRECTION;
import static com.rbc.cm.j65336n1.api.model.Schemas.Order.Meta.Fields.STATE;
import static com.rbc.cm.j65336n1.api.model.Schemas.Order.Meta.Fields.USER;
import static com.rbc.cm.j65336n1.api.model.Schemas.Order.Meta.Fields.RIC;
import static com.rbc.cm.j65336n1.api.model.State.OPEN;
import static java.time.LocalDateTime.now;
import static java.util.Optional.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;
import static reactor.core.publisher.Mono.just;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.rbc.cm.j65336n1.api.model.Direction;
import static com.rbc.cm.j65336n1.api.model.Direction.BUY;
import static com.rbc.cm.j65336n1.api.model.Direction.SELL;
import com.rbc.cm.j65336n1.api.model.Order;
import com.rbc.cm.j65336n1.api.model.RIC;
import com.rbc.cm.j65336n1.api.model.State;
import com.rbc.cm.j65336n1.api.model.User;
import com.rbc.cm.j65336n1.business.RulesEngine;
import com.rbc.cm.j65336n1.middleware.DataDepot;
import com.rbc.cm.j65336n1.tools.Transpiler;

import reactor.core.publisher.Mono;

import static com.rbc.cm.j65336n1.api.model.Utils.parse;
import static com.rbc.cm.j65336n1.api.model.Utils.build_filter_criteria;

import com.rbc.cm.j65336n1.business.RulesEngine.Filter;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@SuppressWarnings({ "unused" })
@Component
public class ApiHandlers {
  /*
   * CREATE
   **/
  public Mono<ServerResponse> create(final ServerRequest rqst) {
    return
      just(rqst)
        .doOnError(t -> _lg.error("", t))
        .flatMap(r -> {
           return r.bodyToMono(Order.class);
         })
        .flatMap(o -> {
           final Optional<Order> o_o = _ddp.create();
           if (!o_o.isPresent()) return failure();

           final Order t = o_o.get();
           if (null == t.id()) return failure();

           o.init(t.id(), OPEN, now());

           _lg.debug("Try matching for [ Order -> [{}]]", _tspr.transpile(o));

           _rngn.execute( of(o))
                .subscribe(x -> {
                   final Order d = x.get();
                   _ddp.update(x);
                   _lg.debug("Matched & Executed [{}]", _tspr.transpile(d));
                 });
               
           return success( of( o.id()));
         })
        .switchIfEmpty(failure());
  }

  /*
   * 
   **/
  public Mono<ServerResponse> retrieve(final ServerRequest request) {
    return
      just(request)
        .doOnError(t -> _lg.error("", t))
        .flatMap(r -> {
           Mono<ServerResponse> mn = null;
           String s_id = null;
           Long id = null;
                    
           try {
             s_id = r.pathVariable( ID );
             id = Long.valueOf(s_id);
           } catch (final IllegalArgumentException t) { mn = wrong(); }
                    
           if (null != mn) return mn;
                    
           final Optional<Long> o_id = of(id);
           final Optional<Boolean> check = _ddp.contains(o_id);
                    
           if (check.isPresent())
             if (check.get().booleanValue()) mn = success(_ddp.get(o_id));
               else mn = missing();
           else mn = failure();
                    
           return mn;
         })
        .switchIfEmpty(failure());
  }

  /*
   * 
   **/
  public Mono<ServerResponse> update(final ServerRequest request) {
    return
      just(request)
        .doOnError(t -> _lg.error("", t))
        .flatMap(r -> {
           return request.bodyToMono(Order.class);
         })
        .flatMap(o -> {
           Mono<ServerResponse> mn = null;
           
           final Optional<Long> o_id = Optional.of(o.id());
           final Optional<Boolean> check = _ddp.contains(o_id);
            
           if (check.isPresent())
             if ( check.get().booleanValue() ) {
               _ddp.update(of(o));
               mn = success( Optional.empty() );
             } else mn = missing();
          else mn = failure();
                 
           return mn;
         })
        .switchIfEmpty(failure());
  }

  /*
   * 
   **/
  public Mono<ServerResponse> delete(final ServerRequest request) {
    return
      just(request)
        .doOnError(t -> _lg.error("", t))
        .flatMap(r -> {
           Mono<ServerResponse> mn = null;
           String s_id = null;
           Long id = null;
                  
           try {
             s_id = r.pathVariable(ID);
             id = Long.valueOf(s_id);
           } catch (final IllegalArgumentException t) { mn = wrong(); }
                  
           if (null != mn) return mn;
                  
           final Optional<Long> o_id = of(id);
           final Optional<Boolean> check = _ddp.contains(o_id);
                  
           if (check.isPresent())
             if (check.get().booleanValue()) {
               _ddp.delete(o_id);
               mn = success( Optional.empty() );
             } else mn = missing();
           else mn = failure();
                  
           return mn;
        }).switchIfEmpty(failure());
  }
    
  /*
   * 
   **/
  public Mono<ServerResponse> search(final ServerRequest request) {
    return
      just(request)
        .doOnError(t -> _lg.error("", t))
        .flatMap(r -> {
           final MultiValueMap<String, String> qry_prms = request.queryParams();

           final String user = qry_prms.getFirst( USER ),
                        drctn_str = qry_prms.getFirst( DIRECTION ),
                        state_str = qry_prms.getFirst( STATE ),
                        ric_str = qry_prms.getFirst( RIC );
               
          final Direction drctn = null != drctn_str && !drctn_str.trim().isEmpty() ? Direction.valueOf(drctn_str)
                                                                                   : null;
          final State state = null != state_str && !state_str.trim().isEmpty() ? State.valueOf(state_str)
                                                                               : null;
          final RIC ric = null != ric_str && !ric_str.trim().isEmpty() ? parse(ric_str)
                                                                       : null;
               
           if (_lg.isDebugEnabled())
             _lg.debug(String.format("Filter params '%s'=[{}], '%s'=[{}], '%s'=[{}], '%s'=[{}]",
                                     RIC, USER, DIRECTION, STATE),
                                     ric, user, drctn, state);

          final Order criteria = build_filter_criteria(ric, drctn, new User(user));
           
          final Optional<Filter> filter = _rngn.build(of(criteria));
           
          if (filter.isPresent())
            return filter.get()
                         .orders()
                         .collectList()
                         .flatMap(o -> success(of(o)));
           

           return failure();
         })
        .switchIfEmpty(failure());
  }
  
  static private Mono<ServerResponse> failure() { return status(INTERNAL_SERVER_ERROR).build(); }
  
  static private Mono<ServerResponse> missing() { return notFound().build(); }
  
  static private Mono<ServerResponse> wrong() { return status(BAD_REQUEST).build(); }
  
  static private <T> Mono<ServerResponse> success(final Optional<T> val) {
    return val.isPresent() ? ok().body( fromObject( val.get()) )
                           : ok().build(); }
  
  @PostConstruct
  void init() {
    assert null != _ddp;
    _lg.info("This [{}] attached to [{}]", toString().substring(toString().lastIndexOf('.') + 1),
                                           _ddp.toString().substring(_ddp.toString().lastIndexOf('.') + 1));
    _lg.info("This [{}] attached to [{}]", toString().substring(toString().lastIndexOf('.') + 1),
                                           _rngn.toString().substring(_rngn.toString().lastIndexOf('.') + 1));
  }

  @Autowired
  private DataDepot<?> _ddp;

  @Autowired
  private RulesEngine _rngn;

  @Autowired
  private Transpiler _tspr;
  
  public ApiHandlers() {/**/}

  final private Logger _lg = LoggerFactory.getLogger(getClass());
}
