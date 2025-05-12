package com.cooknote.backend.global.error.entity;

import org.springframework.http.ResponseEntity;

import com.cooknote.backend.global.error.exceptionCode.AuthErrorCode;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.exceptionCode.JwtErrorCode;

import lombok.Builder;
import lombok.Getter;

// Custom Error 내용을 담을 Response Entity를 생성

@Builder
@Getter
public class ErrorResponseEntity {
	private int status;
	private String name;
	private String message;
	
	public static ResponseEntity<ErrorResponseEntity> toResponseEntity(AuthErrorCode e) {
		return ResponseEntity
				.status(e.getHttpStatus())
				.body(ErrorResponseEntity.builder()
						.status(e.getHttpStatus().value())
						.name(e.name())
						.message(e.getMessage())
						.build());
	}

	public static ResponseEntity<ErrorResponseEntity> toResponseEntity(JwtErrorCode e) {
		return ResponseEntity
				.status(e.getHttpStatus())
				.body(ErrorResponseEntity.builder()
						.status(e.getHttpStatus().value())
						.name(e.name())
						.message(e.getMessage())
						.build());
	}
	
	public static ResponseEntity<ErrorResponseEntity> toResponseEntity(CommonErrorCode e) {
		return ResponseEntity
				.status(e.getHttpStatus())
				.body(ErrorResponseEntity.builder()
						.status(e.getHttpStatus().value())
						.name(e.name())
						.message(e.getMessage())
						.build());
	}
}
