package com.example.springconfigredis.v2;

import com.example.springconfigredis.config.TestValueProperties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "application.config.refresh.auto.v3.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class V3ConfigRefreshScheduler {

  private final ContextRefresher contextRefresher;
  private final TestValueProperties testValueProperties;

  @Scheduled(fixedDelay = 5L, timeUnit = TimeUnit.SECONDS)
  @Async("v3RefreshThreadPoolExecutor")
  public void refreshConfig() {
    try {
      Set<String> refreshedKeys = contextRefresher.refreshEnvironment();
      if (!CollectionUtils.isEmpty(refreshedKeys)) {
        log.info("[Refreshed] " + String.join( ",", refreshedKeys));
        contextRefresher.refresh();
      }
      log.info(testValueProperties.getMy().getValue());
    } catch (Exception e) {
      log.error("config refresh failed");
      log.error("", e);
    }
  }

  @ConditionalOnBean(value = V3ConfigRefreshScheduler.class)
  @Bean
  public ThreadPoolTaskExecutor v3RefreshThreadPoolExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setThreadNamePrefix("v3RefreshThreadPoolExecutor");
    threadPoolTaskExecutor.setCorePoolSize(1);
    threadPoolTaskExecutor.setMaxPoolSize(1);
    threadPoolTaskExecutor.setQueueCapacity(Integer.MAX_VALUE);
    return threadPoolTaskExecutor;
  }



}
