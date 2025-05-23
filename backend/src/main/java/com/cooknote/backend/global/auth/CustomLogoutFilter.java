package com.cooknote.backend.global.auth;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.utils.auth.JwtUtil;
import com.cooknote.backend.global.utils.auth.JWTResponseUtil;
import com.cooknote.backend.global.utils.cookie.CookieUtil;
import com.cooknote.backend.global.utils.redis.RedisUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
	
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}
	
	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		// 로그아웃이 아닌 URI는 다음 필터로 넘김
		if (!request.getRequestURI().equals(Constans.LOGOUT_URI)) {
	        filterChain.doFilter(request, response);
	        return;
	    }

		// POST Method 아니면 다음 필터로 넘김
		if(!request.getMethod().equals(Constans.METHOD_POST_TEXT)) {
			filterChain.doFilter(request, response);
	        return;
		}
		
		String refreshToken = null;

        Cookie[] cookies = request.getCookies();

        // 쿠키 체크
        if (cookies == null) {
        	JWTResponseUtil.writeJson(response, HttpStatus.UNAUTHORIZED.value());
        	return;
        }

        // refreshToekn 가져오기
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(Constans.REFRESH_TOKEN_NAME)) {
            	refreshToken = cookie.getValue();
            }
        }
		
        // refreshToekn 체크
        if(refreshToken == null) {
        	JWTResponseUtil.writeJson(response, HttpStatus.UNAUTHORIZED.value());
        	return;
        }
        
		// 쿠키 삭제
		response.addCookie(CookieUtil.deleteCookie(Constans.REFRESH_TOKEN_NAME));
        
        // 만료 체크
        try {
			jwtUtil.isExpired(refreshToken);
		} catch (Exception e) {
			JWTResponseUtil.writeJson(response, HttpStatus.UNAUTHORIZED.value());
			return;
		}

        // 레디스의 refreshToekn 확인
        long userId = jwtUtil.getUserId(refreshToken);
        String refreshTokenRedisKey = Constans.REFRESH_TOKEN_PREFIX + userId;
		String refreshTokenReidsValue = redisUtil.getData(refreshTokenRedisKey);
				
		if(refreshTokenReidsValue != null) {
			redisUtil.deleteData(refreshTokenRedisKey);
		}
		
		response.setStatus(HttpStatus.OK.value());
	}

}
