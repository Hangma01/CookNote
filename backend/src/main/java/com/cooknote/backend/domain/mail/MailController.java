package com.cooknote.backend.domain.mail;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/email")
public class MailController {
	
	
	// 이메일 조회
	@GetMapping("/verify")
	public ResponseEntity<?> email() throws MessagingException {
		
		
		return null;
	}

	// 이메일로 아이디 찾기
	// 이메일로 비밀번호 찾기
	
	// 이메일 인증 보내기
	// 이메일로 인증 하기
}
