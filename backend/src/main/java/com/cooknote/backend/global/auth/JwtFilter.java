package com.cooknote.backend.global.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.PrintWriter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.message.ErrorMessage;
import com.cooknote.backend.global.utils.auth.JwtUtil;

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
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		
		// 토큰 재발급시 필터 패스
	    if (request.getRequestURI().equals(Constans.REISSUE_URI)
	    		&& request.getMethod().equals(Constans.METHOD_POST_TEXT)) {
	        filterChain.doFilter(request, response);
	        return;
	    }
		
		
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
        try {
        	if(accessToken != null 
        		&& jwtUtil.isValidToken(accessToken)
        		&& jwtUtil.getCategory(accessToken).equals(Constans.ACCESS_TOKEN_NAME)){
    			String userId = jwtUtil.getUserId(accessToken);
            	
            	User user = User.builder()
    			                    .id(userId)
    			                    .build();
            	
            	CustomUserDetails customUserDetails = new CustomUserDetails(user);
            	
            	Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
    			
    			// 현재 Security Context에 설정
            	SecurityContextHolder.getContext().setAuthentication(authentication);
    		}
        } catch (ExpiredJwtException e) {				// Access Token 만료
	    	PrintWriter writer = response.getWriter();
            writer.print(ErrorMessage.ACCESS_TOKEN_EXPIRED_MESSAGE);

	    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    	return;
	    } catch (RuntimeException e) {
	    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    	return;
	    } 
        
        filterChain.doFilter(request, response);
	}

}
