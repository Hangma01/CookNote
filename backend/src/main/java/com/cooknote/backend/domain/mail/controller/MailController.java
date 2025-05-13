package com.cooknote.backend.domain.mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.mail.dto.request.AuthCodeRequestDTO;
import com.cooknote.backend.domain.mail.dto.request.DeleteAuthCodeRequestDTO;
import com.cooknote.backend.domain.mail.dto.request.VerifyAuthCodeRequestDTO;
import com.cooknote.backend.domain.mail.dto.response.VerifyAuthCodeResponseDTO;
import com.cooknote.backend.domain.mail.service.Impl.MailServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

	private final MailServiceImpl mailService;

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
