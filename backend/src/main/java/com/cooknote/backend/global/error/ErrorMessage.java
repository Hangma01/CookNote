package com.cooknote.backend.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

	DUPLICATE_LOGIN_ID("이미 사용중인 아이디 입니다."),
	DUPLICATE_NICKNAME("이미 사용중인 닉네임 입니다."),
	DUPLICATE_EMAIL("이미 사용중인 이메일 입니다.");
	
	private final String message;
}
