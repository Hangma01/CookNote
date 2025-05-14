package com.cooknote.backend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String redisHost;
	
	@Value("${spring.data.redis.port}")
	private int redisPort;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisHost, redisPort);
	}
	
	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
	    
		// Redis를 연결합니다.
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Key-Value 형태로 직렬화를 수행합니다.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        
        return redisTemplate;
	}
	
	
}
