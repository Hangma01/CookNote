package com.cooknote.backend.global.error.excption;

import com.cooknote.backend.global.error.exceptionCode.AuthErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomAuthException extends RuntimeException {
	private final AuthErrorCode errorCode;
}
