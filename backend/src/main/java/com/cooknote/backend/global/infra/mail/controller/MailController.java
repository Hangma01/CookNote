package com.cooknote.backend.global.infra.mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.global.infra.mail.dto.AuthCodeRequestDTO;
import com.cooknote.backend.global.infra.mail.dto.VerifyAuthCodeRequestDTO;
import com.cooknote.backend.global.infra.mail.dto.VerifyAuthCodeResponseDTO;
import com.cooknote.backend.global.infra.mail.service.MailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

	private final MailService mailService;

	// 인증 번호 발송
	@PostMapping("/send/authcode")
	public ResponseEntity<Void> sendAuthCode(@RequestBody AuthCodeRequestDTO authCodeRequestDTO) {
		mailService.sendAuthCode(authCodeRequestDTO.getEmail());
		
		return ResponseEntity.ok().build();	
	}
	
	// 인증 번호 검증
	@PostMapping("/verify")
	public ResponseEntity<VerifyAuthCodeResponseDTO> verifyAuthCode(@RequestBody VerifyAuthCodeRequestDTO verifyAuthCodeRequestDTO) {
	
		return ResponseEntity.ok(mailService.verifyAuthCode(verifyAuthCodeRequestDTO.getEmail(), verifyAuthCodeRequestDTO.getAuthCode()));	
	}
}
