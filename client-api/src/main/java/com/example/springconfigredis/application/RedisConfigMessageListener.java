package com.example.springconfigredis.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisConfigMessageListener implements MessageListener {

  private final Environment environment;

  @Bean("configTopic")
  public ChannelTopic configTopic() {
    return new ChannelTopic("configTopic");
  }


  @Override
  public void onMessage(Message message, byte[] pattern) {
    log.info("onMessage {}", message);

    for (String defaultProfile : environment.getDefaultProfiles()) {
      log.info("defaultProfile: { } ", defaultProfile);
    }
  }
}
