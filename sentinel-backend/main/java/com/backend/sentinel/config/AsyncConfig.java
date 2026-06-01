package com.backend.sentinel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Dedicated thread pool for outbound health-check HTTP requests.
     * Keeps monitoring work isolated from the main request-handling threads.
     *
     * - corePoolSize(10): handles up to 10 concurrent checks without queuing
     * - maxPoolSize(50):  bursts up to 50 when the queue fills
     * - queueCapacity(200): absorbs spikes before spawning extra threads
     */
    @Bean(name = "monitoringExecutor")
    public Executor monitoringExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("sentinel-monitor-");
        executor.initialize();
        return executor;
    }
}
