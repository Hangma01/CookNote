package com.cooknote.backend.domain.auth.service;

import java.io.PrintWriter;
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
import com.cooknote.backend.global.error.exceptionCode.AuthErrorCode;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.exceptionCode.JwtErrorCode;
import com.cooknote.backend.global.error.excption.CustomAuthException;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.error.excption.CustomJwtException;
import com.cooknote.backend.global.message.ErrorMessage;
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
public class AuthService {
	
	private final AuthMapper authMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MailService mailService;
	private final RedisUtil redisUtil;
	private final JwtUtil jwtUtil;
	
	// 아이디 중복 체크
	public void getExistsUserId(String loginId) {
		
		boolean isExists = authMapper.getExistsUserId(loginId);
		
		if(isExists) {
			throw new CustomAuthException(AuthErrorCode.DUPLICATE_USERID_EXCEPTION);
		}
	}
	
	// 닉네임 중복 체크
	public void getExistsNickname(String nickname) {
		
		boolean isExists = authMapper.getExistsNickname(nickname);
		
		if(isExists) {
			throw new CustomAuthException(AuthErrorCode.DUPLICATE_NICKNAME_EXCEPTION);
		}
	}

	
	// 이메일 중복 체크
	public void getExistsEmail(String email) {
		
		boolean isExists = authMapper.getExistsEmail(email);
		
		if(isExists) {
			throw new CustomAuthException(AuthErrorCode.DUPLICATE_EMAIL_EXCEPTION);
		}
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
		
		String mailAuthRedisKey = Constans.MAIL_AUTH_PREFIX + userJoinRequestDTO.getEmail();
		redisUtil.deleteData(mailAuthRedisKey);
	}
	
	
	// 아이디 찾기 - 요청
	public void userFindIdAuthRequest(UserFindIdAuthRequestDTO userFindIdAuthRequestDTO) {
		
		User reqUser = User.builder()
				.name(userFindIdAuthRequestDTO.getName())
				.email(userFindIdAuthRequestDTO.getEmail())
				.build();
		
		Boolean isExistsUser = authMapper.userFindIdAuthRequest(reqUser);
	
		if(!isExistsUser) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_USER_EXCEPTION);
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
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
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
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_USER_EXCEPTION);
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
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		// 비밀번호 재설정 키 확인
		String pwResetRedisKey = Constans.PW_RESET_PREFIX + userFindPwResetRequestDTO.getPwResetToken();
		
		String userId = redisUtil.getData(pwResetRedisKey);
		
		if(userId == null){ 										// 인증 시간 만료
			throw new CustomAuthException(AuthErrorCode.PW_AUTH_EXPIRED_EXCEPTION);
	    }
		
		String encodePw = bCryptPasswordEncoder.encode(userFindPwResetRequestDTO.getNewPw());
		
		
		// 비밀번호 재설정
		User reqUser = User.builder()
				.userId(userId)
				.password(encodePw)
				.build();
		
		authMapper.updatePwReset(reqUser);
		
		// 레디스에 저장된 비밀전호 재설정 key 삭제
		redisUtil.deleteData(pwResetRedisKey);
	}


	// token 재발급
	public void reissue(HttpServletRequest request, HttpServletResponse response) {

		String refreshToken = null;
        
        Cookie[] cookies = request.getCookies();
        
        if (cookies == null) {

        	throw new CustomJwtException(JwtErrorCode.REFRESH_TOKEN_UNAUTHORIZED_EXCEPTION);
        }

        // refreshToekn 가져오기
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
			    		
			    		int accessTokenSecond = Constans.ACCESS_TOKEN_EXPIRED_MS / Constans.SECOND_MS;
			    		int refreshTokenSecond = Constans.REFRESH_TOKEN_EXPIRED_MS / Constans.SECOND_MS;
			    		
			    		response.addCookie(CookieUtil.createCookie(Constans.ACCESS_TOKEN_NAME, newAccessToken, accessTokenSecond));
			    		response.addCookie(CookieUtil.createCookie(Constans.REFRESH_TOKEN_NAME, newRefreshToken, refreshTokenSecond));
			    	 
			    		redisUtil.setDataExpire(refreshTokenRedisKey, newRefreshToken, refreshTokenSecond);
        			}else {
        				throw new CustomJwtException(JwtErrorCode.REFRESH_TOKEN_UNAUTHORIZED_EXCEPTION);
        			}
        		}
        	} catch (RuntimeException e) {
        		throw new CustomJwtException(JwtErrorCode.REFRESH_TOKEN_UNAUTHORIZED_EXCEPTION);
    	    } 
        } else {
        	throw new CustomJwtException(JwtErrorCode.REFRESH_TOKEN_UNAUTHORIZED_EXCEPTION);
        }
	}
}
