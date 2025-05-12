package com.cooknote.backend.domain.auth.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooknote.backend.domain.auth.dto.request.UserFindIdAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserFindPwAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserFindPwResetRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserJoinRequestDTO;
import com.cooknote.backend.domain.auth.dto.response.UserFindIdResponseDTO;
import com.cooknote.backend.domain.auth.dto.response.UserFindPwResponseDTO;
import com.cooknote.backend.domain.mail.service.MailService;
import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.error.CustomException;
import com.cooknote.backend.global.error.ErrorCode;
import com.cooknote.backend.global.utils.auth.JwtUtil;
import com.cooknote.backend.global.utils.cookie.CookieUtil;
import com.cooknote.backend.global.utils.redis.RedisUtil;
import com.cooknote.backend.mappers.AuthMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	
	private final AuthMapper authMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MailService mailService;
	private final RedisUtil redisUtil;
	private final JwtUtil jwtUtil;
	
	// 아이디 중복 체크
	public boolean getCheckUserId(String loginId) {

		return authMapper.getCheckUserId(loginId);
	}

	
	// 닉네임 중복 체크
	public boolean getCheckNickname(String nickname) {

		return authMapper.getCheckNickname(nickname);
	}

	
	// 이메일 중복 체크
	public boolean getCheckEmail(String email) {

		return authMapper.getCheckEmail(email);
	}

	
	// 회원 가입
	@Transactional
	public void userJoin(UserJoinRequestDTO userJoinRequestDTO) {	
	
		String encodePw = bCryptPasswordEncoder.encode(userJoinRequestDTO.getPw());
		
		
		User reqUser = User.builder()
				.userId(userJoinRequestDTO.getUserId())
				.password(encodePw)
				.name(userJoinRequestDTO.getName())
				.nickname(userJoinRequestDTO.getNickname())
				.email(userJoinRequestDTO.getEmail())
				.build();
		
		authMapper.userJoin(reqUser);	
	}
	
	
	// 아이디 찾기 - 요청
	public void userFindIdAuthRequest(UserFindIdAuthRequestDTO userFindIdAuthRequestDTO) {
		
		User reqUser = User.builder()
				.name(userFindIdAuthRequestDTO.getName())
				.email(userFindIdAuthRequestDTO.getEmail())
				.build();
		
		Boolean isExistsUser = authMapper.userFindIdAuthRequest(reqUser);
	
		if(!isExistsUser) {
			throw new CustomException(ErrorCode.NOT_FOUND_USER_EXCEPTION);
		}
		
		mailService.sendAuthCode(userFindIdAuthRequestDTO.getEmail());
	}

	
	// 아이디 찾기 - 아이디 반환
	public UserFindIdResponseDTO userFindId(String name, String email) {
		
		User reqUser = User.builder()
				.name(name)
				.email(email)
				.build();
		
		User rspUser = authMapper.userFindId(reqUser);
		
		if(rspUser == null) {
			throw new CustomException(ErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		UserFindIdResponseDTO userFindIdResponseDTO = UserFindIdResponseDTO.builder()
																		.userId(rspUser.getUserId())
																		.build();
		
		return userFindIdResponseDTO;
	}
	
	// 비밀번호 찾기 - 요청
	public UserFindPwResponseDTO userFindPwAuthRequest(UserFindPwAuthRequestDTO userFindPwAuthRequestDTO) {
		
		User reqUser = User.builder()
				.userId(userFindPwAuthRequestDTO.getUserId())
				.email(userFindPwAuthRequestDTO.getEmail())
				.build();
		
		User rspUser = authMapper.userFindPw(reqUser);
	
		if(rspUser == null) {
			throw new CustomException(ErrorCode.NOT_FOUND_USER_EXCEPTION);
		}
		
		// 메일로 인증코드 송신
		mailService.sendAuthCode(userFindPwAuthRequestDTO.getEmail());

		// 비밀번호 재설정 토큰 추가
		String pwResetToken = UUID.randomUUID().toString();
		String pwResetRedisKey = Constans.PW_RESET_PREFIX + pwResetToken;
		String userId = rspUser.getUserId();
		
		redisUtil.setDataExpire(pwResetRedisKey, userId, Constans.PW_RESET_TOKEN_EXPIRE);
		
		
		UserFindPwResponseDTO userFindPwResponseDTO = UserFindPwResponseDTO.builder()
																		.pwResetToken(pwResetToken)
																		.build();
		
		return userFindPwResponseDTO;
	}
	
	
	// 비밀번호 찾기 - 변경
	@Transactional
	public void userFindPwReset(UserFindPwResetRequestDTO userFindPwResetRequestDTO) {
		
		// 비밀번호 일치 확인
		if(!userFindPwResetRequestDTO.getNewPw().equals(userFindPwResetRequestDTO.getNewPwConfirm())) {
			throw new CustomException(ErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		// 비밀번호 재설정 키 확인
		String pwResetRedisKey = Constans.PW_RESET_PREFIX + userFindPwResetRequestDTO.getPwResetToken();
		
		String userId = redisUtil.getData(pwResetRedisKey);
		
		if(userId == null){ 										// 인증 시간 만료
			throw new CustomException(ErrorCode.PW_AUTH_EXPIRE_EXCEPTION);
	    }
		
		String encodePw = bCryptPasswordEncoder.encode(userFindPwResetRequestDTO.getNewPw());
		
		
		// 비밀번호 재설정
		User reqUser = User.builder()
				.userId(userId)
				.password(encodePw)
				.build();
		
		authMapper.updatePwReset(reqUser);	
	}


	// accessToken 재발급
	public HttpServletResponse reissue(HttpServletRequest request, HttpServletResponse response) {

		String refreshToken = null;
        
        Cookie[] cookies = request.getCookies();
        
        // 쿠키가 없으면 다음 필터로 넘김
        if (cookies == null) {
        	
        	// 배드 리퀘스트
        	return null;
        }

        // accessToekn 가져오기
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(Constans.REFRESH_TOKEN_NAME)) {

            	refreshToken = cookie.getValue();
            }
        }


        // accessToken 유효성 검사
        if(refreshToken != null 
        		&& jwtUtil.getCategory(refreshToken).equals(Constans.REFRESH_TOKEN_NAME)) {
        	
        	try {
        		if(jwtUtil.isValidToken(refreshToken) ) {
        			String userId = jwtUtil.getUserId(refreshToken);
                	String refreshTokenRedisKey = Constans.REFRESH_TOKEN_PREFIX + userId;
        			String refreshTokenReidsValue = redisUtil.getData(refreshTokenRedisKey);
        			
        			if(refreshToken.equals(refreshTokenReidsValue)) {
        				User user = User.builder()
			                    .userId(userId)
			                    .build();
        	
			        	String newAccessToken = jwtUtil.createTokenJwt(Constans.ACCESS_TOKEN_NAME, userId, Constans.ACCESS_TOKEN_EXPIRED_MS);
			    		String newRefreshToken = jwtUtil.createTokenJwt(Constans.REFRESH_TOKEN_NAME, userId, Constans.REFRESH_TOKEN_EXPIRED_MS);
			    		System.out.println("타니?");
			    		response.addCookie(CookieUtil.createCookie(Constans.ACCESS_TOKEN_NAME, newAccessToken));
			    		response.addCookie(CookieUtil.createCookie(Constans.REFRESH_TOKEN_NAME, newRefreshToken));
			    	 
			    		redisUtil.setDataExpire(refreshTokenRedisKey, newRefreshToken, Constans.REFRESH_TOKEN_EXPIRED_MS / Constans.SECOND_MS);
        			}else {
        				// 배드 리퀘스트
        			}
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
        } else {
        	// 배드 리퀘스트
        }
        
        return response;
	}
}
