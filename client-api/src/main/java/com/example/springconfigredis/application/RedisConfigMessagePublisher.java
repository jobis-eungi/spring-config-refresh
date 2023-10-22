package com.example.springconfigredis.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisConfigMessagePublisher {

  private final RedisTemplate<String, String> redisTemplate;
  private final ChannelTopic channelTopic;

  public RedisConfigMessagePublisher(RedisTemplate<String, String> redisTemplate, @Qualifier("configTopic") ChannelTopic channelTopic) {
    this.redisTemplate = redisTemplate;
    this.channelTopic = channelTopic;
  }


  public void publish(String message){
    redisTemplate.convertAndSend(channelTopic.getTopic(), message);
  }

  public void ping(){
    redisTemplate.watch("key");
  }


}
