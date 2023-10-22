package com.example.springconfigredis.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {

  @Value("${server.port}")
  private Long port;

  @Bean("localRestTemplate")
  public RestTemplate localRestTemplate(){
    return new RestTemplateBuilder()
        .setConnectTimeout(Duration.ofSeconds(15))
        .setReadTimeout((Duration.ofSeconds(10)))
        .rootUri("http://localhost:" + port)
        .build();
  }


}
