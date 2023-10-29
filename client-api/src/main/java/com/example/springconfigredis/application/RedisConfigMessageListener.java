package com.example.springconfigredis.application;

import com.example.springconfigredis.config.TestValueProperties;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisConfigMessageListener implements MessageListener {

  private final Environment environment;
  private final RestTemplate restTemplate;
  private final TestValueProperties testValueProperties;
  private final ContextRefresher contextRefresher;

  @Bean("configTopic")
  public ChannelTopic configTopic() {
    return new ChannelTopic("configTopic");
  }


  @Override
  public void onMessage(Message message, byte[] pattern) {
    log.info("onMessage {}", message);

    Set<String> result = refreshWithContextRefresher();
    log.info("================================================ after refresh =================================");
    log.info("[Refreshed] = {}", String.join(",", result));
    log.info(testValueProperties.getMy().getValue());
    log.info("================================================ after refresh =================================");

    for (String defaultProfile : environment.getDefaultProfiles()) {
      log.info("defaultProfile: { } ", defaultProfile);
    }
  }

  @Deprecated
  private String refreshV1WithActuatorEndPoint() {
    return restTemplate.postForObject("/actuator/refresh", null, String.class);
  }

  private Set<String> refreshWithContextRefresher() {
    return contextRefresher.refresh();
  }

}
