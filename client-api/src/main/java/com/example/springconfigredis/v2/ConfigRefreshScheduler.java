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

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "application.config.refresh.auto.v2.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class ConfigRefreshScheduler {

  private final ContextRefresher contextRefresher;
  private final TestValueProperties testValueProperties;

  @Scheduled(fixedDelay = 5L, timeUnit = TimeUnit.SECONDS)
  @Async("refreshThreadPoolExecutor")
  public void refreshConfig() {
    try {
      Set<String> refresh = contextRefresher.refresh();
      log.info(testValueProperties.getMy().getValue());
      refresh.forEach(log::info);
    } catch (Exception e) {
      log.error("config refresh failed");
      log.error("", e);
    }
  }

  @ConditionalOnBean(value = ConfigRefreshScheduler.class)
  @Bean
  public ThreadPoolTaskExecutor refreshThreadPoolExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setThreadNamePrefix("refreshThreadPoolExecutor");
    threadPoolTaskExecutor.setCorePoolSize(1);
    threadPoolTaskExecutor.setMaxPoolSize(1);
    threadPoolTaskExecutor.setQueueCapacity(Integer.MAX_VALUE);
    return threadPoolTaskExecutor;
  }


}
