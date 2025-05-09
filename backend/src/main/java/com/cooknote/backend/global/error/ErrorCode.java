package com.cooknote.backend.global.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	
	// 중복 에러
	DUPLICATE_USERID_EXCEPTION(HttpStatus.CONFLICT, "이미 사용중인 아이디 입니다."),
	DUPLICATE_NICKNAME_EXCEPTION(HttpStatus.CONFLICT, "이미 사용중인 닉네임 입니다."),
	DUPLICATE_EMAIL_EXCEPTION(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다."),
	
	// 인증 코드 시간 만료
	MAIL_AUTH_CODE_EXPIRE_EXCEPTION(HttpStatus.GONE, "인증코드 시간이 만료되었습니다. 인증 번호를 재전송 후 인증 해주세요."),
	
	// 유효성 검사
	VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 입력을 요청하셨습니다. 잠시 후 다시 시도해주세요."),
	
	NOT_FOUND_USER_ID_EXCEPTION(HttpStatus.NOT_FOUND, "이름 또는 이메일을 잘못 입력하셨습니다."),
	INVALID_STATE_EXCEPTION(HttpStatus.NOT_FOUND, "정상적인 요청이 아닙니다.");
	
	private final HttpStatus httpStatus;
	private final String message;
	
}