package com.cooknote.backend.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
	
	// 메일 - 인증 코드 검증 에러 메시지
	VERIFY_AUTH_CODE_EXPIRE("인증코드 시간이 만료되었습니다."),
	VERIFY_AUTH_CODE_NOT_MATCH("인증코드가 일치하지 않습니다.");
	
	private final String message;
}
