package com.cooknote.backend.global.error.entity;

import org.springframework.http.ResponseEntity;

import com.cooknote.backend.global.error.exceptionCode.AuthErrorCode;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.exceptionCode.GoogleErrorCode;
import com.cooknote.backend.global.error.exceptionCode.JwtErrorCode;
import com.cooknote.backend.global.error.exceptionCode.RecipeErrorCode;
import com.cooknote.backend.global.error.exceptionCode.S3ErrorCode;
import com.cooknote.backend.global.error.exceptionCode.UserErrorCode;

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
	
	public static ResponseEntity<ErrorResponseEntity> toResponseEntity(S3ErrorCode e) {
		return ResponseEntity
				.status(e.getHttpStatus())
				.body(ErrorResponseEntity.builder()
						.status(e.getHttpStatus().value())
						.name(e.name())
						.message(e.getMessage())
						.build());
	}
	
	public static ResponseEntity<ErrorResponseEntity> toResponseEntity(RecipeErrorCode e) {
		return ResponseEntity
				.status(e.getHttpStatus())
				.body(ErrorResponseEntity.builder()
						.status(e.getHttpStatus().value())
						.name(e.name())
						.message(e.getMessage())
						.build());
	}
	
	public static ResponseEntity<ErrorResponseEntity> toResponseEntity(UserErrorCode e) {
		return ResponseEntity
				.status(e.getHttpStatus())
				.body(ErrorResponseEntity.builder()
						.status(e.getHttpStatus().value())
						.name(e.name())
						.message(e.getMessage())
						.build());
	}
	
	public static ResponseEntity<ErrorResponseEntity> toResponseEntity(GoogleErrorCode e) {
		return ResponseEntity
				.status(e.getHttpStatus())
				.body(ErrorResponseEntity.builder()
						.status(e.getHttpStatus().value())
						.name(e.name())
						.message(e.getMessage())
						.build());
	}
}
