package com.cooknote.backend.domain.user.service.impl;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserMapper userMapper;
	
	// 유저 조회
	@Override
	public User getUser(String id) {

		return userMapper.getUser(id);
	}
	
	
}
