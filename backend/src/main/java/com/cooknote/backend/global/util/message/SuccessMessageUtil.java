package com.cooknote.backend.global.util.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessageUtil {
	
	VERIFY_AUTH_CODE_SUCCESS("인증이 성공했습니다.");
	
	private final String message;
}
