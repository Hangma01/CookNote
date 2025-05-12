package com.cooknote.backend.global.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorCode {
	PW_AUTH_EXPIRE_EXCEPTION(HttpStatus.GONE, "비밀번호 변경 시간이 만료되었습니다. 다시 시도해주세요.");
	
	
	private final HttpStatus httpStatus;
	private final String message;
}
