package com.cooknote.backend.global.message;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
	
	// 메일 - 인증 코드 검증 에러 메시지
	VERIFY_AUTH_CODE_EXPIRE("인증코드 시간이 만료되었습니다."),
	VERIFY_AUTH_CODE_NOT_MATCH("인증코드가 일치하지 않습니다."),
	
	LOGIN_FAIL_MESSAGE("아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요"),
	SUSPEND_USER_EXCEPTION("정지된 아이디입니다. 관리자에게 문의하세요."),
	BALACKLIST_EXCEPTION("Blacklist"),
	ACCESS_TOKEN_EXPIRED_MESSAGE("AccessToken Expired");
	
	private final String message;
}
