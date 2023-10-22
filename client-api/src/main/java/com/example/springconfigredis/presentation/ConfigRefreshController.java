package com.example.springconfigredis.presentation;

import com.example.springconfigredis.application.RedisConfigMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/refresh")
public class ConfigRefreshController {

  private final RedisConfigMessagePublisher redisConfigMessagePublisher;

  @GetMapping("/ping")
  public String ping(){
    redisConfigMessagePublisher.ping();
    return "ping success";
  }

  @PostMapping("/publish/test")
  public String publish(){
    redisConfigMessagePublisher.publish("test");
    return "test publish success";
  }

}
