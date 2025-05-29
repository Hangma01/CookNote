package com.cooknote.backend.domain.auth.service.impl;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.domain.user.enums.UserStatus;
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.auth.CustomUserDetails;
import com.cooknote.backend.global.message.ErrorMessage;
import com.cooknote.backend.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	
	private final UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

		// DB에서 유저 조회
		User getUser = userService.getLoginUser(id, UserStatus.DELETE);

		// 유저 조회 실패
		if(getUser == null) {

			throw new UsernameNotFoundException(ErrorMessage.LOGIN_FAIL_MESSAGE.getMessage());
		}
		
		// UserDetails에 담아서 return 하면 AuthenticationManager가 검증함
		return new CustomUserDetails(getUser);
	}
}
