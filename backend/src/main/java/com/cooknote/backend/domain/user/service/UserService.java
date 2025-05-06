package com.cooknote.backend.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooknote.backend.domain.user.dto.UserDTO;
import com.cooknote.backend.global.error.CustomException;
import com.cooknote.backend.global.error.ErrorCode;
import com.cooknote.backend.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;
	
	
	// 아이디 중복 체크
	public boolean getCheckLoginId(String loginId) {

		return userMapper.getCheckLoginId(loginId);
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
	public void userJoin(UserDTO userDTO) {
		userMapper.userJoin(userDTO);	
	}
}
