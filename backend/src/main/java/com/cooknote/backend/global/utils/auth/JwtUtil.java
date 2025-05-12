package com.cooknote.backend.global.utils.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cooknote.backend.global.constants.Constans;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {
	
	private final SecretKey secretKey;


    public JwtUtil(@Value("${jwt.secret}")String secret) {
    	
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get(Constans.USER_ID_NAME, String.class);
    }    
    
    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }


    
   //JWT 토큰 생성
    public String createTokenJwt(String category, String userId, long expiredMs) {
    	
		return Jwts.builder()
					.claim(Constans.CATEGORY_NAME, category)
					.claim(Constans.USER_ID_NAME, userId)
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis() + expiredMs))
					.signWith(secretKey)
					.compact();
   }

    
    // 토큰 정보를 검증하는 메서드
    public boolean isValidToken(String token) {
        Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);
        
        return true;
    }
}
