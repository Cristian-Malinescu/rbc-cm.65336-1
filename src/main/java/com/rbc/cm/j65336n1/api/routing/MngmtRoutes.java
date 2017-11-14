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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@SuppressWarnings("static-method")
@Configuration
public class MngmtRoutes {

  @Bean
  RouterFunction<ServerResponse> mappings(final MngmtHandlers apiHdlr) {
    return route(GET("/"), apiHdlr::heartbeat).
             andRoute(GET("/api"), apiHdlr::heartbeat).
             andRoute(GET("/api/v1"), apiHdlr::heartbeat).
             andRoute(GET("/heartbeat"), apiHdlr::heartbeat).
             andRoute(GET("/status"), apiHdlr::status);
  }

  public MngmtRoutes() {}
}
