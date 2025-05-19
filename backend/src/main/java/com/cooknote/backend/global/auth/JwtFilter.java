package com.cooknote.backend.global.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.PrintWriter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.message.ErrorMessage;
import com.cooknote.backend.global.utils.auth.JwtUtil;
import com.cooknote.backend.global.utils.response.ResponseUtil;

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

	private final JwtUtil jwtUtil;
    
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	

		// 필터 패스 조건		
	    String accessToken= resolveToken(request);
	    if (accessToken == null) {
	    	filterChain.doFilter(request, response);
				
            return;
	    }
	    log.info("유효성검사한디");
        // accessToken 유효성 검사
        try {
        	if(accessToken != null 
        		&& jwtUtil.isValidToken(accessToken)){
        		
        		String id = jwtUtil.getId(accessToken);
    			long userId = jwtUtil.getUserId(accessToken);
            	
            	User user = User.builder()
            						.userId(userId)
    			                    .id(id)
    			                    .build();
            	
            	CustomUserDetails customUserDetails = new CustomUserDetails(user);
            	
            	Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
    			
    			// 현재 Security Context에 설정
            	SecurityContextHolder.getContext().setAuthentication(authentication);
    		}
        } catch (ExpiredJwtException e) {				// Access Token 만료
        	   log.info("accessToken 만료됨");
	    	ResponseUtil.writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorMessage.ACCESS_TOKEN_EXPIRED_MESSAGE.getMessage());
	    	
	    	return;
	    } catch (RuntimeException e) {
	    	log.error("accessToken 다른 에러네", e);
	    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    	return;
	    } 
        
        filterChain.doFilter(request, response);
	}

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constans.AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constans.BEARER_PREFIX)) return bearerToken.substring(7);

        return null;
    }
}
