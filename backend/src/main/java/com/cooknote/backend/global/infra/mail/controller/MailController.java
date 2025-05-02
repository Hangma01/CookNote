package com.cooknote.backend.global.infra.mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.infra.mail.service.MailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {
	
	private final MailService mailService;

	
	// 인증코드 발송
	@GetMapping("/send/authcode")
	public ResponseEntity<Void> sendAuthCode(String to)  {
		
		try {
			mailService.sendAuthCode(to);
		} catch (MessagingException e) {
			
		}
		return null;
	}

	// 이메일로 아이디 찾기
	// 이메일로 비밀번호 찾기
	
	// 이메일 인증 보내기
	// 이메일로 인증 하기
}
