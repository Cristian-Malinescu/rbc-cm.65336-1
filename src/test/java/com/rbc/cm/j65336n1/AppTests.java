package com.rbc.cm.j65336n1;

import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import com.rbc.cm.j65336n1.api.model.HeartBeat;
import com.rbc.cm.j65336n1.api.model.Order;

@RunWith(SpringRunner.class)
public class AppTests {
	@Test
	public void contextLoads() {
	}

	@Test
	public void basicRootAccess() {
    webClient.get()
             .uri("/", new Object[] {})
             .accept(APPLICATION_JSON_UTF8)
             .exchange()
             .block()
             .bodyToMono(HeartBeat.class)
             .doOnSuccess( r -> {
               assertNotNull(r);
             })
             .doOnError( e -> {
               log.error(getStackTrace(e));
             })
             .block();
	}
	
  @Test
  public void basicOrderAccess() {
    webClient.get()
             .uri("/api/v1/order?user=User1&direction=SELL&state=OPEN", new Object[] {})
             .accept(APPLICATION_JSON_UTF8)
             .exchange()
             .block()
             .bodyToMono(Collection.class)
             .doOnSuccess( o -> {
                assertNotNull(o);
              })
             .doOnError( e -> {
                log.error(getStackTrace(e));
              })
             .block();
  }

  @BeforeClass
  static public void setup() {
    webClient = WebClient.builder()
                         .baseUrl("http://localhost:8080")
                         .build();
  }
  
  //@Autowired
  static WebClient webClient;
  
  Logger log = LoggerFactory.getLogger(getClass());
}
