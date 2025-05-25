package com.cooknote.backend.global.utils.common;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cooknote.backend.global.auth.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonFunctionUtil {

	public static boolean validationCheck(BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
                log.error("Field Error - Field: {}, Message: {}", error.getField(), error.getDefaultMessage());
            }
			return true;
		}
		
		return false;
	}
	
	public static boolean nullCheck(Long check) {
		
		if(check == null) {
			return true;
		}
		
		return false;
	}
	
	public static boolean authCheck(CustomUserDetails customUserDetails) {
		
		if(customUserDetails != null) {
			return true;
		}
		
		return false;
	}
}
