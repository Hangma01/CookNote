package com.cooknote.backend.global.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	
	
	// 로그인 에러
	// 로그인 에러 - 회원 가입 중복 체크
	DUPLICATE_USERID_EXCEPTION(HttpStatus.CONFLICT, "이미 사용중인 아이디 입니다."),
	DUPLICATE_NICKNAME_EXCEPTION(HttpStatus.CONFLICT, "이미 사용중인 닉네임 입니다."),
	DUPLICATE_EMAIL_EXCEPTION(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다."),

		
	// 인증 - 메일 인증 코드 시간 만료
	MAIL_AUTH_CODE_EXPIRE_EXCEPTION(HttpStatus.GONE, "인증코드 시간이 만료되었습니다. 인증 번호를 재전송 후 인증 해주세요."),
	
	
	// 유효성 검사
	VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 입력을 요청하셨습니다. 잠시 후 다시 시도해주세요."),

	
	// 사용자 정보 찾지 못할 시 에러
	NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
	LOGIN_NOT_FOUN_USER_EXCEPTIOON(HttpStatus.NOT_FOUND, "아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요."),
	
	
	// 비정상 요청 시 에러
	INVALID_STATE_EXCEPTION(HttpStatus.BAD_REQUEST, "정상적인 요청이 아닙니다."),
	
	
	// 비밀번호 찾기 - 변경 에러
	PW_AUTH_EXPIRE_EXCEPTION(HttpStatus.GONE, "비밀번호 변경 시간이 만료되었습니다. 다시 시도해주세요.");
	
	
	private final HttpStatus httpStatus;
	private final String message;
	
}