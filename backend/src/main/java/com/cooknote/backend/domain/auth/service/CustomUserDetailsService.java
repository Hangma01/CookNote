package com.cooknote.backend.domain.auth.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.global.auth.CustomUserDetails;
import com.cooknote.backend.global.message.ErrorMessage;
import com.cooknote.backend.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		// DB에서 유저 조회
		User userData = userMapper.getUser(userId);

		if(userData == null) {

			throw new UsernameNotFoundException(ErrorMessage.LOGIN_FAIL_MESSAGE.getMessage());
		}
		
		// UserDetails에 담아서 return 하면 AuthenticationManager가 검증함
		return new CustomUserDetails(userData);
	}
}
