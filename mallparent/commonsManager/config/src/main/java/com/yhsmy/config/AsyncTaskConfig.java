package com.yhsmy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务线程池
 *
 * @auth 李正义
 * @date 2020/2/14 15:28
 **/
@Configuration
@EnableAsync
public class AsyncTaskConfig {

    @Bean
    public Executor executor () {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor ();
        executor.setThreadNamePrefix ("executor-thread-");
        executor.setMaxPoolSize (20);
        executor.setCorePoolSize (15);
        executor.setQueueCapacity (0);
        executor.setRejectedExecutionHandler (new ThreadPoolExecutor.CallerRunsPolicy ());
        return executor;
    }

}
