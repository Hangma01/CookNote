package com.cooknote.backend.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // 모든 @Controller 즉, 전역에서 발생할 수 있는 예외를 잡아 처리한다.
public class CustonExceptionHandler {

	// 발생한 CustomException 예외를 잡아서 하나의 메소드에서 공통 처리한다.
	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
		return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
	}
}
