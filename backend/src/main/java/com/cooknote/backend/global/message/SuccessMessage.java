package com.cooknote.backend.global.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	
	VERIFY_AUTH_CODE_SUCCESS("인증이 성공했습니다.");
	
	private final String message;
}
