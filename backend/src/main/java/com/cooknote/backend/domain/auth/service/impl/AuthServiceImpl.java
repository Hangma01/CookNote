package com.cooknote.backend.domain.auth.service.impl;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.auth.dto.request.UserFindIdAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserFindPwAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserFindPwResetRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserJoinRequestDTO;
import com.cooknote.backend.domain.auth.dto.response.UserFindIdResponseDTO;
import com.cooknote.backend.domain.auth.dto.response.UserFindPwResponseDTO;
import com.cooknote.backend.domain.auth.service.AuthService;
import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.error.exceptionCode.AuthErrorCode;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.exceptionCode.JwtErrorCode;
import com.cooknote.backend.global.error.excption.CustomAuthException;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.error.excption.CustomJwtException;
import com.cooknote.backend.global.infra.mail.service.MailService;
import com.cooknote.backend.global.infra.mail.service.impl.MailServiceImpl;
import com.cooknote.backend.global.utils.auth.JwtUtil;
import com.cooknote.backend.global.utils.cookie.CookieUtil;
import com.cooknote.backend.global.utils.redis.RedisUtil;
import com.cooknote.backend.mappers.AuthMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
	
	private final AuthMapper authMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MailService mailService;
	private final RedisUtil redisUtil;
	private final JwtUtil jwtUtil;
	
	// 아이디 중복 체크
	@Override
	public void getExistsId(String id) {
		boolean isExists = authMapper.getExistsId(id);
		
		if(isExists) {
			throw new CustomAuthException(AuthErrorCode.DUPLICATE_USERID_EXCEPTION);
		}
	}
	
	// 닉네임 중복 체크
	@Override
	public void getExistsNickname(String nickname) {
		boolean isExists = authMapper.getExistsNickname(nickname);
		
		if(isExists) {
			throw new CustomAuthException(AuthErrorCode.DUPLICATE_NICKNAME_EXCEPTION);
		}
	}
	
	// 이메일 중복 체크
	@Override
	public void getExistsEmail(String email) {
		boolean isExists = authMapper.getExistsEmail(email);
		
		if(isExists) {
			throw new CustomAuthException(AuthErrorCode.DUPLICATE_EMAIL_EXCEPTION);
		}
	}
	
	// 회원가입
	@Override
	public void userJoin(UserJoinRequestDTO userJoinRequestDTO) {
		
		log.info(userJoinRequestDTO.toString());
		// 비밀번호 일치 확인
		if(!userJoinRequestDTO.getPw().equals(userJoinRequestDTO.getPwConfirm())) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		String password = bCryptPasswordEncoder.encode(userJoinRequestDTO.getPw());
		
		
		User reqUser = User.builder()
				.id(userJoinRequestDTO.getId())
				.password(password)
				.name(userJoinRequestDTO.getName())
				.nickname(userJoinRequestDTO.getNickname())
				.email(userJoinRequestDTO.getEmail())
				.profileImage(Constans.DEFALUT_PROFILE_IMAGE)
				.build();
		
		authMapper.userJoin(reqUser);	
		
		String mailAuthRedisKey = Constans.MAIL_AUTH_PREFIX + userJoinRequestDTO.getEmail();
		redisUtil.deleteData(mailAuthRedisKey);
	}
	
	// 아이디 찾기 - 메일 인증 요청
	@Override
	public void userFindIdAuthRequest(UserFindIdAuthRequestDTO userFindIdAuthRequestDTO) {
		
		Boolean isExistsUser = authMapper.userFindIdAuthRequest(userFindIdAuthRequestDTO.getName(), 
																userFindIdAuthRequestDTO.getEmail());
	
		if(!isExistsUser) {
			throw new CustomAuthException(AuthErrorCode.NOT_FOUND_USER_EXCEPTION);
		}
		
		mailService.sendAuthCode(userFindIdAuthRequestDTO.getEmail());

	}
	
	// 아이디 찾기 - 아이디 반환
	@Override
	public UserFindIdResponseDTO userFindId(String name, String email) {

		User rspUser = authMapper.userFindId(name, email);
		
		if(rspUser == null) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		UserFindIdResponseDTO userFindIdResponseDTO = UserFindIdResponseDTO.builder()
																		.id(rspUser.getId())
																		.build();
		
		return userFindIdResponseDTO;
	}
	
	// 비밀번호 찾기 - 비밀번호 요청
	@Override
	public UserFindPwResponseDTO userFindPwAuthRequest(UserFindPwAuthRequestDTO userFindPwAuthRequestDTO) {
		

		User rspUser = authMapper.userFindPw(userFindPwAuthRequestDTO.getId(), userFindPwAuthRequestDTO.getEmail());
		
		System.out.println(rspUser);
		
		if(rspUser == null) {
			throw new CustomAuthException(AuthErrorCode.NOT_FOUND_USER_EXCEPTION);
		}
		
		// 메일로 인증코드 송신
		mailService.sendAuthCode(userFindPwAuthRequestDTO.getEmail());

		// 비밀번호 재설정 토큰 추가
		String pwResetToken = UUID.randomUUID().toString();
		String pwResetRedisKey = Constans.PW_RESET_PREFIX + pwResetToken;
		long userId = rspUser.getUserId();
		
		redisUtil.setDataExpire(pwResetRedisKey, String.valueOf(userId), Constans.PW_RESET_TOKEN_EXPIRE);
		
		
		UserFindPwResponseDTO userFindPwResponseDTO = UserFindPwResponseDTO.builder()
																		.pwResetToken(pwResetToken)
																		.build();
		
		return userFindPwResponseDTO;
	}
	
	// 비밀번호 찾기 - 재설정
	@Override
	public void userFindPwReset(UserFindPwResetRequestDTO userFindPwResetRequestDTO) {
		// 비밀번호 일치 확인
		if(!userFindPwResetRequestDTO.getNewPw().equals(userFindPwResetRequestDTO.getNewPwConfirm())) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		// 비밀번호 재설정 키 확인
		String pwResetRedisKey = Constans.PW_RESET_PREFIX + userFindPwResetRequestDTO.getPwResetToken();
		
		String getUserId = redisUtil.getData(pwResetRedisKey);
		
		if(getUserId == null){ 										// 인증 시간 만료
			throw new CustomAuthException(AuthErrorCode.PW_AUTH_EXPIRED_EXCEPTION);
	    }
		
		String password = bCryptPasswordEncoder.encode(userFindPwResetRequestDTO.getNewPw());
		long userId = Long.valueOf(getUserId);
		
		authMapper.updatePwReset(userId, password);
		
		// 레디스에 저장된 비밀전호 재설정 key 삭제
		redisUtil.deleteData(pwResetRedisKey);
	}
	
	// AccessToken 재발급
	@Override
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

        // refreshToekn 유효성 검사
    	try {
    		if(refreshToken != null && jwtUtil.isValidToken(refreshToken)) {
    			String id = jwtUtil.getId(refreshToken);
    			long userId = jwtUtil.getUserId(refreshToken);
    			
    			

            	String refreshTokenRedisKey = Constans.REFRESH_TOKEN_PREFIX + userId;
    			String refreshTokenReidsValue = redisUtil.getData(refreshTokenRedisKey);
    			
    			// 레디스에 있는 RefreshToken과 비교하기    			
    			if(refreshToken.equals(refreshTokenReidsValue)) {
    				
    				// 새로운 토큰 생성
		        	String newAccessToken = jwtUtil.createTokenJwt(userId, id, Constans.ACCESS_TOKEN_EXPIRED_MS);
		    		String newRefreshToken = jwtUtil.createTokenJwt(userId, id, Constans.REFRESH_TOKEN_EXPIRED_MS);
		    		
		    		int refreshTokenSecond = Constans.REFRESH_TOKEN_EXPIRED_MS / Constans.SECOND_MS;
		    		
		    		// 쿠키 변경
		    		response.addHeader(Constans.AUTHORIZATION_HEADER, Constans.BEARER_PREFIX + newAccessToken);
		    		response.addCookie(CookieUtil.createCookie(Constans.REFRESH_TOKEN_NAME, newRefreshToken, refreshTokenSecond));
		    	 
		    		// 새로운 RefreshToken 레디스에 저장
		    		redisUtil.setDataExpire(refreshTokenRedisKey, newRefreshToken, refreshTokenSecond);
    			}else {
    				throw new CustomJwtException(JwtErrorCode.REFRESH_TOKEN_UNAUTHORIZED_EXCEPTION);
    			}
    		}
    	} catch (RuntimeException e) {
    		throw new CustomJwtException(JwtErrorCode.REFRESH_TOKEN_UNAUTHORIZED_EXCEPTION);
	    } 

	}
}
