package com.cooknote.backend.global.infra.mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.global.infra.mail.dto.request.AuthCodeRequestDTO;
import com.cooknote.backend.global.infra.mail.dto.request.VerifyAuthCodeRequestDTO;
import com.cooknote.backend.global.infra.mail.dto.response.VerifyAuthCodeResponseDTO;
import com.cooknote.backend.global.infra.mail.service.MailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

	private final MailService mailService;

	// 인증 번호 발송
	@PostMapping("/sendAuthcode")
	public ResponseEntity<Void> sendAuthCode(@RequestBody AuthCodeRequestDTO authCodeRequestDTO) {
		mailService.sendAuthCode(authCodeRequestDTO.getEmail());
		
		return ResponseEntity.ok().build();	
	}
	
	// 인증 번호 검증
	@PostMapping("/verify")
	public ResponseEntity<VerifyAuthCodeResponseDTO> verifyAuthCode(@RequestBody VerifyAuthCodeRequestDTO verifyAuthCodeRequestDTO) {
	
		return ResponseEntity.ok(mailService.verifyAuthCode(verifyAuthCodeRequestDTO.getEmail(), verifyAuthCodeRequestDTO.getAuthCode()));	
	}
	
	@DeleteMapping("/authCode")
	public ResponseEntity<Void> deleteAuthCode(@RequestParam("email") String email) {
		mailService.deleteAuthCode(email);
		
		return ResponseEntity.ok().build();	
	}
}
