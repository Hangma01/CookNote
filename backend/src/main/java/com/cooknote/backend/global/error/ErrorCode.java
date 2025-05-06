package com.cooknote.backend.global.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	
	// 인증 코드 시간 만료
	MAIL_AUTH_CODE_EXPIRE(HttpStatus.GONE, "인증코드 시간이 만료되었습니다. 인증 번호를 재전송 후 인증 해주세요.");
	
	private final HttpStatus httpStatus;
	private final String message;
	
}
