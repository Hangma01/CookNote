package com.cooknote.backend.global.auth;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cooknote.backend.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	
	private final User user;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {

		return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getId();
	}
	
	public long getUserId() {
		
		return user.getUserId();
	}
	
	// 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {

        return true;
    }
}
