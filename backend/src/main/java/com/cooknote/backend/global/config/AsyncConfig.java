package com.cooknote.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
	@Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  	// 코어 쓰레드 풀, 동시에 실행할 수 있는 최소 쓰레드 수
        executor.setMaxPoolSize(50); 	// 최대 쓰레드 풀, 동시에 실행할 수 있는 최대 쓰레드 수
        executor.setQueueCapacity(100); // 쓰레드 풀을 넘는 요청이 들어올 때 처리하는 대기열 즉, 큐에 100개까지 대기 가능
        executor.setThreadNamePrefix("Email Send Async Task-");
        executor.initialize(); 			// 설정한 속성들 적용
        return executor;
    }
}
