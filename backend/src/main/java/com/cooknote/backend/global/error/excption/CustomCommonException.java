package com.cooknote.backend.global.error.excption;

import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomCommonException extends RuntimeException {
	private final CommonErrorCode commonErrorCode;
}
