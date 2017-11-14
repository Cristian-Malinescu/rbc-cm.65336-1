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

import static java.lang.Long.valueOf;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.rbc.cm.j65336n1.LifeCycleBaseEvent;
import com.rbc.cm.j65336n1.middleware.DataDepot.RepositoryBaseEvent;
import com.rbc.cm.j65336n1.middleware.DataDepot.RepositoryFlushEvent;
import com.rbc.cm.j65336n1.middleware.DataDepot.RepositoryLoadEvent;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@Component
public class DataIdFactory<E extends LifeCycleBaseEvent> implements ApplicationListener<E> {
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
    reset();
  }

  private void flush() {
    _lg.debug("DataDepot <Flush> notification received.");
    reset();
  }

  private void reset() { _gntr = new AtomicLong(); }

  public Long next_id() { return valueOf(_gntr.getAndIncrement()) ; }
  
  /**
   * Sequence Generator
   */
  private AtomicLong _gntr;
  
  public DataIdFactory() {}

  final private Logger _lg = LoggerFactory.getLogger(getClass());
}
