package com.cooknote.backend.domain.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooknote.backend.domain.user.dto.User;
import com.cooknote.backend.domain.user.dto.UserJoinRequestDTO;
import com.cooknote.backend.global.error.CustomException;
import com.cooknote.backend.global.error.ErrorCode;
import com.cooknote.backend.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
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
		
		User user = User.builder()
				.userId(userJoinRequestDTO.getUserId())
				.password(encodePassword)
				.name(userJoinRequestDTO.getName())
				.nickname(userJoinRequestDTO.getNickname())
				.email(userJoinRequestDTO.getEmail())
				.build();
		
		userMapper.userJoin(user);	
	}
}
