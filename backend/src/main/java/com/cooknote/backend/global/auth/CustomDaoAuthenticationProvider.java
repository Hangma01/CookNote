package com.cooknote.backend.global.auth;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {
	
	public CustomDaoAuthenticationProvider() {
		this.setHideUserNotFoundExceptions(false);
	}

}
