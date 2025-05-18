package com.cooknote.backend.global.error.exceptionCode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode {
	// 유효성 검사
	VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 입력을 요청하셨습니다. 잠시 후 다시 시도해주세요."),
	
	// 비정상 요청 시 에러
	INVALID_STATE_EXCEPTION(HttpStatus.BAD_REQUEST, "정상적인 요청이 아닙니다."),
	
	// 사용자 정보 찾지 못할 시 에러
	NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
	
	UPDATE_EXCEPTION(HttpStatus.BAD_REQUEST,"저장에 실패했습니다. 다시 시도해주세요.");
	
	private final HttpStatus httpStatus;
	private final String message;
}