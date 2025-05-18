package com.cooknote.backend.global.error.excption;

import com.cooknote.backend.global.error.exceptionCode.S3ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomS3Exception extends RuntimeException {
	private final S3ErrorCode s3ErrorCode;
}
