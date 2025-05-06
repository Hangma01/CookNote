package com.cooknote.backend.global.infra.utils.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessageUtil {

	// 유저 - 중복 검증 에러 메시지
	DUPLICATE_LOGIN_ID("이미 사용중인 아이디 입니다."),
	DUPLICATE_NICKNAME("이미 사용중인 닉네임 입니다."),
	DUPLICATE_EMAIL("이미 사용중인 이메일 입니다."),
	
	// 메일 - 인증 코드 검증 에러 메시지
	VERIFY_AUTH_CODE_EXPIRE("인증코드 시간이 만료되었습니다."),
	VERIFY_AUTH_CODE_NOT_MATCH("인증코드가 일치하지 않습니다.");
	
	private final String message;
}
