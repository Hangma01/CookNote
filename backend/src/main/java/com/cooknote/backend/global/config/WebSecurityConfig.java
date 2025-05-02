package com.cooknote.backend.global.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource)
			throws Exception {
		http
				// cors 설정
				.cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration configuration = new CorsConfiguration();

						configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
						configuration.setAllowedMethods(Collections.singletonList("*"));
						configuration.setAllowCredentials(true);
						configuration.setAllowedHeaders(Collections.singletonList("*"));
						configuration.setMaxAge(3600L);

						configuration.setExposedHeaders(Collections.singletonList("Authorization"));

						return configuration;
					}
				}))

				// csrf disable
				.csrf(AbstractHttpConfigurer::disable)

				// form 로그인 방식 disable
				.formLogin(AbstractHttpConfigurer::disable)

				// http basic 인증 방식 disable
				.httpBasic(AbstractHttpConfigurer::disable)

				// 인가 설정
				.authorizeHttpRequests((auth) -> auth
						.anyRequest().permitAll())
//				.authorizeHttpRequests((auth) -> auth
//						.requestMatchers("/*").permitAll()
//						.anyRequest().authenticated())

				// 세션 설정
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// LoginFilter() 전에 JWTFilter() 추가
				//.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)

				// 필터 추가 LoginFilter()는 인자를 받음
//				.addFilterAt(
//						new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisRepository),
//						UsernamePasswordAuthenticationFilter.class)

				// LogoutFilter() 전에 CustomLogoutFilter() 추가
//				.addFilterBefore(new CustomLogoutFilter(jwtUtil, redisRepository), LogoutFilter.class)

				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

}
