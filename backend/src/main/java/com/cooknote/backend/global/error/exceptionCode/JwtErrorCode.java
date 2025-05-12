package com.cooknote.backend.global.error.exceptionCode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorCode {
	REFRESH_TOKEN_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인 시간이 만료되었습니다. 다시 로그인 해주세요.");
	
	
	private final HttpStatus httpStatus;
	private final String message;
}
