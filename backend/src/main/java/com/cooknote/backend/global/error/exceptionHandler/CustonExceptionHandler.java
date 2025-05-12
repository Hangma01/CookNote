package com.cooknote.backend.global.error.exceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cooknote.backend.global.error.entity.ErrorResponseEntity;
import com.cooknote.backend.global.error.excption.CustomAuthException;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.error.excption.CustomJwtException;

@ControllerAdvice // 모든 @Controller 즉, 전역에서 발생할 수 있는 예외를 잡아 처리한다.
public class CustonExceptionHandler {

	// 발생한 CustomException 예외를 잡아서 하나의 메소드에서 공통 처리한다.
	@ExceptionHandler(CustomAuthException.class)
	protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomAuthException e) {

		return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
	}
	
	@ExceptionHandler(CustomJwtException.class)
	protected ResponseEntity<ErrorResponseEntity> handleJwtException(CustomJwtException e) {

		return ErrorResponseEntity.toResponseEntity(e.getJwtErrorCode());
	}
	
	@ExceptionHandler(CustomCommonException.class)
	protected ResponseEntity<ErrorResponseEntity> handleCommonException(CustomCommonException e) {

		return ErrorResponseEntity.toResponseEntity(e.getCommonErrorCode());
	}
}
