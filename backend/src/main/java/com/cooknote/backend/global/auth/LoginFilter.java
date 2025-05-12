package com.cooknote.backend.global.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.utils.auth.JWTUtil;
import com.cooknote.backend.global.utils.cookie.CookieUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


// 로그인 검증을 위한 커스텀
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	
    @Value("${jwt.expiredMs.accessToken}")
    private long accessTokenExpiredMs;

    @Value("${jwt.expiredMs.refreshToken}")
    private long refreshTokenExpiredMs;
	
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	
	 @Override
	 public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		 
		 // 클라이언트 요청에서 userId, password 추출
		 setUsernameParameter(Constans.USER_ID_NAME);
		 String userId = obtainUsername(request);
		 String password = obtainPassword(request);
		 
		 // SpringSecurity에서 userId와 password를 검증하기 위해 token에 정보 담기
		 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password, null);
		 
		 // 검증을 위해 AuthenticationManager에 authToken 전달
		 return authenticationManager.authenticate(authToken);
	 }
	 
	// 로그인 성공 시 실행하는 메서드
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
		
		String userId = authentication.getName();
		
		String accessToken = jwtUtil.createTokenJwt(Constans.ACCESS_TOKEN_NAME, userId, accessTokenExpiredMs);
		String refreshToen = jwtUtil.createTokenJwt(Constans.REFRESH_TOKEN_NAME, userId, refreshTokenExpiredMs);
		
		response.addCookie(CookieUtil.createCookie(Constans.ACCESS_TOKEN_NAME, accessToken));
		response.addCookie(CookieUtil.createCookie(Constans.REFRESH_TOKEN_NAME, refreshToen));
		
		response.setStatus(HttpStatus.OK.value());
	}
	
	// 로그인 실패 시 실행하는 메서드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
}
