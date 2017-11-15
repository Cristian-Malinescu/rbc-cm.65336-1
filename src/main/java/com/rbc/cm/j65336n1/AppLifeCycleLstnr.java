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
package com.rbc.cm.j65336n1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import com.rbc.cm.j65336n1.middleware.DataDepot.RepositoryFlushEvent;
import com.rbc.cm.j65336n1.middleware.DataDepot.RepositoryLoadEvent;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@Component
public class AppLifeCycleLstnr<E extends ApplicationEvent> implements ApplicationListener<E> {

  @Override
  public void onApplicationEvent(final E evt) {
    if (evt instanceof SpringApplicationEvent)
      if (ApplicationReadyEvent.class.isInstance(evt))
        on_application_ready( (ApplicationReadyEvent) evt);
    
    if (evt instanceof ApplicationContextEvent) {
      if (ContextStartedEvent.class.isInstance(evt))
        on_context_started((ContextStartedEvent) evt);

      if (ContextRefreshedEvent.class.isInstance(evt))
        on_context_refreshed((ContextRefreshedEvent) evt);

      if (ContextStoppedEvent.class.isInstance(evt))
        on_context_stopped((ContextStoppedEvent) evt);
      
      if (ContextClosedEvent.class.isInstance(evt))
        on_context_closed((ContextClosedEvent) evt);
    }
  }
  
  private void on_application_ready(final ApplicationReadyEvent evt) {
    _ln.debug("App <Ready> notification received.", evt);
    applicationEventPublisher.publishEvent(new RepositoryLoadEvent(this));
  }

  private void on_context_started(final ContextStartedEvent evt) {
    _ln.debug("Context <Started> notification received.", evt);
  }

  private void on_context_refreshed(final ContextRefreshedEvent evt) {
    _ln.debug("Context <Refreshed> notification received.", evt);
  }

  private void on_context_stopped(final ContextStoppedEvent evt) {
    _ln.debug("Context <Stopped> notification received.", evt);
  }

  private void on_context_closed(final ContextClosedEvent evt) {
    _ln.debug("Context <Closed> notification received.", evt);
    applicationEventPublisher.publishEvent(new RepositoryFlushEvent(this));
  }
  
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  public AppLifeCycleLstnr() {
    _ln.info("Instance [{}] created.", this);
  }

  final private Logger _ln = LoggerFactory.getLogger(getClass());
}
