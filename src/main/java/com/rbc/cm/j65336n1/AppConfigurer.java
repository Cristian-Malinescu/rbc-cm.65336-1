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

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.zalando.jackson.datatype.money.MoneyModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Cristian Malinescu
 * @since 1.0.0
 */
@Configuration
public class AppConfigurer implements WebFluxConfigurer {

  @Bean
  JavaTimeModule javatimeModule() { return new JavaTimeModule(); }

  @Bean
  Jdk8Module jdk8Module() { return new Jdk8Module(); }
  
  @Bean
  MoneyModule moneyModule() { return new MoneyModule(); }
    
  @Bean
  Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.featuresToEnable(WRITE_DATES_AS_TIMESTAMPS);
  }

  @Bean
  Jackson2JsonEncoder jackson2JsonEncoder(final ObjectMapper mapper) {
    return new Jackson2JsonEncoder(mapper);
  }

  @Bean
  Jackson2JsonDecoder jackson2JsonDecoder(final ObjectMapper mapper) {
    return new Jackson2JsonDecoder(mapper);
  }

  @Bean
  WebFluxConfigurer webFluxConfigurer(final Jackson2JsonEncoder encoder, final Jackson2JsonDecoder decoder) {
    return new WebFluxConfigurer() {
      @Override
      public void configureHttpMessageCodecs(final ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(encoder);
        configurer.defaultCodecs().jackson2JsonDecoder(decoder);
      }
    };
  }

  public AppConfigurer() {}

}
