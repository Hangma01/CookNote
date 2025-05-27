package com.cooknote.backend.global.error.exceptionCode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode { 
	PW_UPDATE_MATCH_EXCEPTION(HttpStatus.BAD_REQUEST, "현재 비밀번호와 동일합니다."); // 현재 비밀번호화 변경할 비밀번호가 일치 할 때
	
	private final HttpStatus httpStatus;
	private final String message;
}
