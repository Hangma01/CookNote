package com.cooknote.backend.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용중인 아이디 입니다.");
	
	private final HttpStatus httpStatus;
	private final String message;
	
}
