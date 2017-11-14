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

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.rbc.cm.j65336n1.api.model.HeartBeat;

import reactor.core.publisher.Mono;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@SuppressWarnings({ "static-method", "unused" })
@Component
public class MngmtHandlers {
  public Mono<ServerResponse> heartbeat(final ServerRequest request) {
    final Mono<HeartBeat> mono = just(new HeartBeat());

    return mono.flatMap(tic -> ok().contentType(APPLICATION_JSON_UTF8).body(fromObject(tic)))
               .switchIfEmpty(notFound().build());
  }

  public Mono<ServerResponse> status(final ServerRequest request) {
    final Mono<HeartBeat> mono = just(new HeartBeat());

    return mono.flatMap(tic -> ok().contentType(APPLICATION_JSON_UTF8).body(fromObject(tic)))
        .switchIfEmpty(notFound().build());
  }
    
  public MngmtHandlers() {/**/}
}
