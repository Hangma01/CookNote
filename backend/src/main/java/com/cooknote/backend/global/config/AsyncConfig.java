package com.cooknote.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
	
	private static final int CORE_POOL_SIZE = 10;
	private static final int MAX_POOL_SIZE = 30;
	private static final int QUEUE_CAPACITY = 1000;
	private static final String THREAD_NAME_PREFIX = "email-send-async-task";
	
	@Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);  	// 코어 쓰레드 풀, 동시에 실행할 수 있는 최소 쓰레드 수
        executor.setMaxPoolSize(MAX_POOL_SIZE); 	// 최대 쓰레드 풀, 동시에 실행할 수 있는 최대 쓰레드 수
        executor.setQueueCapacity(QUEUE_CAPACITY); // 쓰레드 풀을 넘는 요청이 들어올 때 처리하는 대기열 즉, 큐에 100개까지 대기 가능
        executor.setThreadNamePrefix("THREAD_NAME_PREFIX");
        executor.initialize(); 			// 설정한 속성들 적용
        return executor;
    }
}
