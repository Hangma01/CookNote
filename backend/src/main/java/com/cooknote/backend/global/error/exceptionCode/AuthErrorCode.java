package com.cooknote.backend.global.error.exceptionCode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode {
	
	
	//  회원 가입 중복 체크
	DUPLICATE_USERID_EXCEPTION(HttpStatus.CONFLICT, "이미 사용중인 아이디 입니다."),
	DUPLICATE_NICKNAME_EXCEPTION(HttpStatus.CONFLICT, "이미 사용중인 닉네임 입니다."),
	DUPLICATE_EMAIL_EXCEPTION(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다."),
		

	// 사용자 정보 찾지 못할 시 에러
	NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
	
	// 인증 - 메일 인증 코드 시간 만료
	MAIL_AUTH_CODE_EXPIRED_EXCEPTION(HttpStatus.GONE, "인증코드 시간이 만료되었습니다. 인증 번호를 재전송 후 인증 해주세요."),
	
	// 비밀번호 찾기 - 변경 에러
	PW_AUTH_EXPIRED_EXCEPTION(HttpStatus.GONE, "비밀번호 변경 시간이 만료되었습니다. 다시 시도해주세요.");
	

	
	
	private final HttpStatus httpStatus;
	private final String message;
	
}