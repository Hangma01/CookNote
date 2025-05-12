package com.cooknote.backend.global.error.excption;

import com.cooknote.backend.global.error.exceptionCode.JwtErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomJwtException extends RuntimeException {
	private final JwtErrorCode jwtErrorCode;
}
