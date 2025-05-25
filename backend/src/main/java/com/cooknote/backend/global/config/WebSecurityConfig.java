package com.cooknote.backend.global.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.cooknote.backend.global.auth.JwtFilter;
import com.cooknote.backend.global.auth.CustomLoginFilter;
import com.cooknote.backend.global.auth.CustomLogoutFilter;
import com.cooknote.backend.global.utils.auth.JwtUtil;
import com.cooknote.backend.global.utils.redis.RedisUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;
	
    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
	
    // 비밀번호 암호화
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
		
		// cors 설정
		http
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
			}));
		
		
		// csrf disable
		http
			.csrf(AbstractHttpConfigurer::disable);
		
		
		// form 로그인 방식 disable
		http
			.formLogin(AbstractHttpConfigurer::disable);
		
		
		// form 로그아웃 방식 disable
		http
			.logout(AbstractHttpConfigurer::disable);
		
		
		// http basic 인증 방식 disable
		http
			.httpBasic(AbstractHttpConfigurer::disable);

		
		// 경로별 인가 작업
		http
			.authorizeHttpRequests((auth) -> auth
						.requestMatchers("/login", "/logout", "/auth/**", "/mail/**","/s3/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/recipe/*", "/comment", "/comment/replies", "/user/profile/host", "/recipe/public/host").permitAll()
						.anyRequest().authenticated());

		
		// 로그인 필터 전 JWTFilter 등록
	    http
           .addFilterBefore(new JwtFilter(jwtUtil), CustomLoginFilter.class);
	    
	    
		// 필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager()는 직접 new로 만들 수 없어서 authenticationConfiguration가 필요함)
		http
			.addFilterBefore(new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisUtil), UsernamePasswordAuthenticationFilter.class);

		
		// 필터 추가 LogoutFilter
		http
			.addFilterBefore(new CustomLogoutFilter(jwtUtil, redisUtil), LogoutFilter.class);
		
		
		// 세션 설정
		http
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


		return http.build();
	}

}
