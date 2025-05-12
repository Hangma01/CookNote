package com.cooknote.backend.global.auth;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.utils.auth.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
        String accessToken = null;

        Cookie[] cookies = request.getCookies();
        
        // 쿠키가 없으면 다음 필터로 넘김
        if (cookies == null) {
        	filterChain.doFilter(request, response);
        	
        	return;
        }

        // accessToekn 가져오기
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(Constans.ACCESS_TOKEN_NAME)) {

            	accessToken = cookie.getValue();
            }
        }


        // accessToken 유효성 검사
        if(accessToken != null 
        		&& jwtUtil.getCategory(Constans.ACCESS_TOKEN_NAME).equals(Constans.ACCESS_TOKEN_NAME)) {
        	
        	try {
        		if(jwtUtil.isValidToken(accessToken) ) {
        			String userId = jwtUtil.getUserId(accessToken);
                	
                	User user = User.builder()
        			                    .userId(userId)
        			                    .build();
                	
                	CustomUserDetails customUserDetails = new CustomUserDetails(user);
                	
                	Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
        			
        			// 현재 Security Context에 설정
                	SecurityContextHolder.getContext().setAuthentication(authentication);
        		}
        			
        	} catch (SecurityException | MalformedJwtException | SignatureException e) {
    	        log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    	    } catch (ExpiredJwtException e) {
    	        log.error("Expired JWT token, 만료된 JWT token 입니다.");
    	    } catch (UnsupportedJwtException e) {
    	        log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    	    } catch (IllegalArgumentException e) {
    	        log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    	    }
        }
        
        filterChain.doFilter(request, response);
	}

}
