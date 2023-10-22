package com.example.springconfigredis.config;

import com.example.springconfigredis.application.RedisConfigMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisPubSubConfig {

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    return new JedisConnectionFactory();
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(jedisConnectionFactory());
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    return template;
  }

  @Bean
  public RedisMessageListenerContainer redisMessageListenerContainer(RedisConfigMessageListener redisConfigMessageListener) {
    RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
    redisMessageListenerContainer.setConnectionFactory(jedisConnectionFactory());
    redisMessageListenerContainer.addMessageListener(redisConfigMessageListener, redisConfigMessageListener.configTopic());
    return redisMessageListenerContainer;
  }


}
