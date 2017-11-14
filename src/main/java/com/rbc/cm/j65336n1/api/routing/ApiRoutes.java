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

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.rbc.cm.j65336n1.api.model.Schemas.Order.Meta.Fields;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@Configuration
public class ApiRoutes {
  /*
   * CREATE
   **/
  @Bean
  RouterFunction<ServerResponse> post(final ApiHandlers apiHdlr) {
    return route(POST(api_model_path()), apiHdlr::create);
  }

  /*
   * RETRIEVE
   **/
  @Bean
  RouterFunction<ServerResponse> get(final ApiHandlers apiHdlr) {
    return route(GET(api_model_path() + id_plc_hldr), apiHdlr::retrieve);
  }

  /*
   * UPDATE
   **/
  @Bean
  RouterFunction<ServerResponse> put(final ApiHandlers apiHdlr) {
    return route(PUT(api_model_path()), apiHdlr::update);
  }

  /*
   * DELETE
   **/
  @Bean
  RouterFunction<ServerResponse> delete(final ApiHandlers apiHdlr) {
    return route(DELETE(api_model_path() + id_plc_hldr), apiHdlr::delete);
  }

  /*
   * ALL/SEARCH
   **/
  @Bean
  RouterFunction<ServerResponse> search(final ApiHandlers apiHdlr) {
    return route(GET(api_model_path()), apiHdlr::search);
  }
  
  private String api_model_path() { return api_base_sgmnt + api_model_sgmnt; }

  final private String api_base_sgmnt = "/api/v1";
  final private String api_model_sgmnt = "/order",
                       id_plc_hldr = "/{" + Fields.ID + "}";

  public ApiRoutes() {}
}
