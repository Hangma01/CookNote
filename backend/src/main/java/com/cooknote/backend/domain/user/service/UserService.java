package com.cooknote.backend.domain.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooknote.backend.domain.user.dto.request.UserFindIdAuthRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserJoinRequestDTO;
import com.cooknote.backend.domain.user.dto.response.UserFindIdResponseDTO;
import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.global.error.CustomException;
import com.cooknote.backend.global.error.ErrorCode;
import com.cooknote.backend.global.infra.mail.service.MailService;
import com.cooknote.backend.global.infra.utils.DateFormatUtil;
import com.cooknote.backend.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MailService mailService;
	
	
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
	
		String encodePassword = bCryptPasswordEncoder.encode(userJoinRequestDTO.getPassword());
		
		
		User reqUser = User.builder()
				.userId(userJoinRequestDTO.getUserId())
				.password(encodePassword)
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
			throw new CustomException(ErrorCode.NOT_FOUND_USER_ID_EXCEPTION);
		}
		
		mailService.sendAuthCode(userFindIdAuthRequestDTO.getEmail());
	}

	
	// 아이디 찾기
	public UserFindIdResponseDTO userFindId(String name, String email) {
		
		User reqUser = User.builder()
				.name(name)
				.email(email)
				.build();
		
		User rspUser = userMapper.userFindId(reqUser);
		
		if(rspUser == null) {
			// 잘못된 요처응로 바꿔야함
			throw new CustomException(ErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		UserFindIdResponseDTO userFindIdResponseDTO = UserFindIdResponseDTO.builder()
																		.userId(rspUser.getUserId())
																		.build();
		
		return userFindIdResponseDTO;
	}
}
