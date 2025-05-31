package com.cooknote.backend.global.error.exceptionCode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoogleErrorCode {
	NOT_FOUND_THUMBNAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "썸네일이 존재하지 않습니다");
	
	
	private final HttpStatus httpStatus;
	private final String message;
}
