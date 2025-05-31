package com.cooknote.backend.global.error.excption;


import com.cooknote.backend.global.error.exceptionCode.GoogleErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomGoogleException extends RuntimeException {
	private final GoogleErrorCode googleErrorCode;
}
