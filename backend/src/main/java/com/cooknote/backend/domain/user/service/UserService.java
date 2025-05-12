package com.cooknote.backend.domain.user.service;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooknote.backend.domain.mail.service.MailService;
import com.cooknote.backend.domain.user.dto.request.UserFindIdAuthRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserFindPwAuthRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserFindPwResetRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserJoinRequestDTO;
import com.cooknote.backend.domain.user.dto.response.UserFindIdResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserFindPwResponseDTO;
import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.error.CustomException;
import com.cooknote.backend.global.error.ErrorCode;
import com.cooknote.backend.global.util.redis.RedisUtil;
import com.cooknote.backend.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MailService mailService;
	private final RedisUtil redisUtil;
	
	
	// 아이디 중복 체크
	public boolean getCheckUserId(String loginId) {

		return userMapper.getCheckUserId(loginId);
	}

	
	// 닉네임 중복 체크
	public boolean getCheckNickname(String nickname) {

		return userMapper.getCheckNickname(nickname);
	}

	
	// 이메일 중복 체크
	public boolean getCheckEmail(String email) {

		return userMapper.getCheckEmail(email);
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
		
		userMapper.userJoin(reqUser);	
	}
	
	
	// 아이디 찾기 - 요청
	public void userFindIdAuthRequest(UserFindIdAuthRequestDTO userFindIdAuthRequestDTO) {
		
		User reqUser = User.builder()
				.name(userFindIdAuthRequestDTO.getName())
				.email(userFindIdAuthRequestDTO.getEmail())
				.build();
		
		Boolean isExistsUser = userMapper.userFindIdAuthRequest(reqUser);
	
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
		
		User rspUser = userMapper.userFindId(reqUser);
		
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
		
		User rspUser = userMapper.userFindPw(reqUser);
	
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
		
		userMapper.updatePwReset(reqUser);	
	}



}
