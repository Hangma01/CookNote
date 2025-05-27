package com.cooknote.backend.global.error.excption;

import com.cooknote.backend.global.error.exceptionCode.UserErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserException extends RuntimeException {
	private final UserErrorCode userErrorCode;
}
