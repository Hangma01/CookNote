package com.cooknote.backend.global.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;
	private final JwtErrorCode jwtErrorCode;
	
    // ErrorCode만 받는 생성자
    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.jwtErrorCode = null;  // JwtErrorCode는 null로 설정
    }

    // JwtErrorCode만 받는 생성자
    public CustomException(JwtErrorCode jwtErrorCode) {
        this.errorCode = null;  // ErrorCode는 null로 설정
        this.jwtErrorCode = jwtErrorCode;
    }
}
