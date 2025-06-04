package com.cooknote.backend.global.auth;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cooknote.backend.domain.auth.dto.request.UserLoginRequestDTO;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.error.exceptionCode.UserErrorCode;
import com.cooknote.backend.global.message.ErrorMessage;
import com.cooknote.backend.global.utils.auth.JwtUtil;
import com.cooknote.backend.global.utils.common.CommonFunctionUtil;
import com.cooknote.backend.global.utils.common.ResponseUtil;
import com.cooknote.backend.global.utils.cookie.CookieUtil;
import com.cooknote.backend.global.utils.redis.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// 로그인 검증을 위한 커스텀
@RequiredArgsConstructor
@Slf4j
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
	
	
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;
	
	 @Override
	 public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		 
		
		// JSON => Java로 역직렬화
		ObjectMapper objectMapper = new ObjectMapper();
		UserLoginRequestDTO loginRequest = null;
		try {
			loginRequest = objectMapper.readValue(request.getInputStream(), UserLoginRequestDTO.class);
		} catch (IOException e) {
			log.error("로그인 JSON 파싱 오류: {}", e.getMessage());
		}
		String id = loginRequest.getId();
		String password = loginRequest.getPassword();
		
		// SpringSecurity에서 userId와 password를 검증하기 위해 token에 정보 담기
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password, null);
		
		// 검증을 위해 AuthenticationManager에 authToken 전달
		return authenticationManager.authenticate(authToken);
	 }
	 
	// 로그인 성공 시 실행하는 메서드
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
		
		
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

		// user 정보 가져오기
        long userId = customUserDetails.getUserId();
        String id = customUserDetails.getUsername();
		
        // 토큰 생성
		String accessToken = jwtUtil.createTokenJwt(userId, id, Constans.ACCESS_TOKEN_EXPIRED_MS);
		String refreshToKen = jwtUtil.createTokenJwt(userId, id, Constans.REFRESH_TOKEN_EXPIRED_MS);
		
		int refreshTokenSecond = Constans.REFRESH_TOKEN_EXPIRED_MS / Constans.SECOND_MS;
		
		// 레디스에 refresh 토큰 저장
		String refreshTokenRedisKey = Constans.REFRESH_TOKEN_PREFIX + userId; 
		redisUtil.setDataExpire(refreshTokenRedisKey, refreshToKen, refreshTokenSecond);
		
		// 블랙 리스트 삭제
		String delBlacklist = Constans.BLACKLIST_PREFIX + userId;
		redisUtil.deleteData(delBlacklist);
		
		// 쿠키 생성 및 응답 반환
		response.addHeader(Constans.AUTHORIZATION_HEADER, Constans.BEARER_PREFIX + accessToken);
		response.addCookie(CookieUtil.createCookie(Constans.REFRESH_TOKEN_NAME, refreshToKen, refreshTokenSecond));
		response.setStatus(HttpStatus.OK.value());
	}
	
	// 로그인 실패 시 실행하는 메서드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

		
		String message = failed.getMessage();
		
		if(CommonFunctionUtil.match(message, ErrorMessage.SUSPEND_USER_EXCEPTION.getMessage())) {
			ResponseUtil.writeJson(response, HttpServletResponse.SC_UNAUTHORIZED,  failed.getMessage());
		} else {
			ResponseUtil.writeJson(response, HttpServletResponse.SC_UNAUTHORIZED,  ErrorMessage.LOGIN_FAIL_MESSAGE.getMessage());
		}
	
		
//		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
}
