package com.cooknote.backend.domain.user.service;

import org.springframework.stereotype.Service;

import com.cooknote.backend.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;
	
	
	// 아이디 중복 체크
	public boolean getExistsLoginId(String loginId) {

		return userMapper.getExistsLoginId(loginId);
	}

}
