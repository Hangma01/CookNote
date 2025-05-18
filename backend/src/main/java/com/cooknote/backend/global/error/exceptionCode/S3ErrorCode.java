package com.cooknote.backend.global.error.exceptionCode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum S3ErrorCode {
	EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, "이미지 파일 존재하지 않습니다."),		// 파일이 없을 때
	IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.BAD_REQUEST, "이미지 업로드 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요."), // 업로드 시래
	NO_FILE_EXTENTION(HttpStatus.BAD_REQUEST, "파일에 확장자가 없습니다. 유효한 이미지 파일을 업로드해주세요."), // 확장자 없을 때
	INVALID_FILE_EXTENTION(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다. (지원 형식: jpg, jpeg, png, webp)"), // 지원 파일 형식이 아닐떄
	PUT_OBJECT_EXCEPTION(HttpStatus.BAD_REQUEST, "이미지를 저장하는 데 실패했습니다. 잠시 후 다시 시도해주세요."), // 저장 실패
	IO_EXCEPTION_ON_IMAGE_DELETE(HttpStatus.BAD_REQUEST, "이미지를 삭제하는 도중 오류가 발생했습니다."); // 삭제 오류
	
	
	private final HttpStatus httpStatus;
	private final String message;
}
