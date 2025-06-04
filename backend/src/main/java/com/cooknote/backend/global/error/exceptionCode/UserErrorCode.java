package com.cooknote.backend.global.error.exceptionCode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode { 
	PW_UPDATE_MATCH_EXCEPTION(HttpStatus.BAD_REQUEST, "현재 비밀번호와 동일합니다."), // 현재 비밀번호화 변경할 비밀번호가 일치 할 때
	REPORT_DUPLICATION_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 신고를 하셨습니다."),	// 중복 신고 시 에러
	SUSPEND_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "정지된 사용자입니다.");
	
	private final HttpStatus httpStatus;
	private final String message;
}
