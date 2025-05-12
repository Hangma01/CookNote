package com.cooknote.backend.global.error.excption;

import com.cooknote.backend.global.error.exceptionCode.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;
}
