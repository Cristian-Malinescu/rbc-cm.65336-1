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
package com.rbc.cm.j65336n1.middleware;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.rbc.cm.j65336n1.LifeCycleBaseEvent;
import com.rbc.cm.j65336n1.api.model.Order;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@Component
public class DataDepot<E extends LifeCycleBaseEvent> implements ApplicationListener<E> {
  @Override
  public void onApplicationEvent(final E evt) {
    if (evt instanceof RepositoryBaseEvent) {
      if (RepositoryLoadEvent.class.isInstance(evt))
        load();

      if (RepositoryFlushEvent.class.isInstance(evt))
        flush();
    }
  }
  
  private void load() {
    _lg.debug("DataDepot <Load> notification received.");
    new DataLoader(this).load(_load_all);
  }
  
  private void flush() {
    _lg.debug("DataDepot <Flush> notificatio" + "n received.");
    _cache.clear();
  }
  
  /*
   * 
   **/
  public Optional<Order> create() {
    final Long id = _id_fctry.next_id();
    _cache.put(id, new Order(id));
    return of(_cache.get(id));
  }

  /*
   * 
   **/
  public Optional<Order> get(final Optional<Long> id) {
    if (id.isPresent()) return ofNullable(_cache.get(id.get()));
    return Optional.empty();
  }

  /*
   * 
   **/
  public Optional<Collection<Order>> get() { return of(_cache.values()); }

  /*
   * 
   **/
  public void update(final Optional<Order> val) {
    val.ifPresent(o -> _cache.put(o.id(), o));
  }

  /*
   * 
   **/
  public void delete(final Optional<Long> val) {
    val.ifPresent( id -> _cache.remove(id) );
  }
  
  public Optional<Boolean> contains(final Optional<Long> id) {
    if (id.isPresent()) return Optional.of(Boolean.valueOf(_cache.containsKey(id.get())));
    
    return Optional.of(Boolean.FALSE);
  }

  @Value("${load_all_dev_test_data}")
  private final boolean _load_all = true;

  @PostConstruct
  private void setup() {
    _cache = new ConcurrentSkipListMap<>();
  }

  @Autowired
  private DataIdFactory<?> _id_fctry;

  private ConcurrentMap<Long, Order> _cache;
  
  public DataDepot() {}

  static abstract class RepositoryBaseEvent extends LifeCycleBaseEvent {
    RepositoryBaseEvent(final Object source) { super(source); }

    static final private long serialVersionUID = 1L;
  }
  
  static final public class RepositoryLoadEvent extends RepositoryBaseEvent {
    public RepositoryLoadEvent(final Object source) { super(source); }

    static final private long serialVersionUID = 1L;
  }

  static final public class RepositoryFlushEvent extends RepositoryBaseEvent {
    public RepositoryFlushEvent(final Object source) { super(source); }

    static final private long serialVersionUID = 1L;
  }

  final private Logger _lg = LoggerFactory.getLogger(getClass());
}
