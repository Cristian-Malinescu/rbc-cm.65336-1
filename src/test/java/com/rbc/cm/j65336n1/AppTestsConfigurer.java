package com.rbc.cm.j65336n1;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.zalando.jackson.datatype.money.MoneyModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//@TestConfiguration
public class AppTestsConfigurer implements WebFluxConfigurer {

  // @Bean
  JavaTimeModule javatimeModule() { return new JavaTimeModule(); }

  // @Bean
  Jdk8Module jdk8Module() { return new Jdk8Module(); }
  
  // @Bean
  MoneyModule moneyModule() { return new MoneyModule(); }
    
  // @Bean
  Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.featuresToEnable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  // @Bean
  Jackson2JsonEncoder jackson2JsonEncoder(final ObjectMapper mapper) {
    return new Jackson2JsonEncoder(mapper);
  }

  // @Bean
  Jackson2JsonDecoder jackson2JsonDecoder(final ObjectMapper mapper) {
    return new Jackson2JsonDecoder(mapper);
  }

  // @Bean
  WebFluxConfigurer webFluxConfigurer(final Jackson2JsonEncoder encoder, final Jackson2JsonDecoder decoder) {
    return new WebFluxConfigurer() {
      @Override
      public void configureHttpMessageCodecs(final ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(encoder);
        configurer.defaultCodecs().jackson2JsonDecoder(decoder);
      }
    };
  }

  public AppTestsConfigurer() {}

}
