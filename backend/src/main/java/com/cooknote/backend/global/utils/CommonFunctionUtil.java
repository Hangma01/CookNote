package com.cooknote.backend.global.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cooknote.backend.global.auth.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonFunctionUtil {
    private static final int MAIL_AUTH_CODE_LEN = 6;
    private static final SecureRandom secureRandom = new SecureRandom();
    
    
    // 유효성 체크
	public static boolean validationCheck(BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
                log.error("Field Error - Field: {}, Message: {}", error.getField(), error.getDefaultMessage());
            }
			return true;
		}
		
		return false;
	}
	
	// Long 타입 NUll 체크
	public static boolean nullCheck(Long check) {
		
		if(check == null) {
			return true;
		}
		
		return false;
	}
	
	
	// 인증 여부 체크
	public static boolean authCheck(CustomUserDetails customUserDetails) {
		
		if(customUserDetails != null) {
			return true;
		}
		
		return false;
	}
	
	
	// 데이터 포맷
	public static String dateFormat(LocalDateTime date) {
		
		String formatDate = date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		
		return formatDate;
	}

	// 메일 인증 코드 생성
    public static String createMailAuthCode() {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < MAIL_AUTH_CODE_LEN; i++) {
            key.append(secureRandom.nextInt(10));
        }

        return key.toString();
    }
    
    // 문자열 매치 
    public static boolean match(String input, String target) {
        if (input == null || target == null) {
            return false;
        }
        return input.equals(target);
    }
}
