package com.example.springconfigredis.application;

import com.example.springconfigredis.config.TestValueProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RedisConfigMessageListener implements MessageListener {

  private final Environment environment;
  private final RestTemplate restTemplate;
  private final TestValueProperties testValueProperties;

  @Value("${my.value}")
  private String myValue;

  public RedisConfigMessageListener(Environment environment, RestTemplate restTemplate,
      TestValueProperties testValueProperties) {
    this.environment = environment;
    this.restTemplate = restTemplate;
    this.testValueProperties = testValueProperties;
  }

  @Bean("configTopic")
  public ChannelTopic configTopic() {
    return new ChannelTopic("configTopic");
  }


  @Override
  public void onMessage(Message message, byte[] pattern) {
    log.info("onMessage {}", message);

    String result = refreshV1WithActuatorEndPoint();
    log.info("================================================ after refresh =================================");
    log.info("result = {}", result);
    log.info("refreshValue by @ConfigurationOnProperties {}", testValueProperties.getMy());
    log.info("refreshValue by @Value {}", myValue);
    log.info("================================================ after refresh =================================");

    for (String defaultProfile : environment.getDefaultProfiles()) {
      log.info("defaultProfile: { } ", defaultProfile);
    }
  }

  private String refreshV1WithActuatorEndPoint() {
    return restTemplate.postForObject("/actuator/refresh", null, String.class);
  }

}
