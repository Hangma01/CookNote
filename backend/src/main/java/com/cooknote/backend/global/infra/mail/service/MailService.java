package com.cooknote.backend.global.infra.mail.service;

import org.springframework.stereotype.Service;

import com.cooknote.backend.global.infra.mail.dto.response.VerifyAuthCodeResponseDTO;

@Service
public interface MailService {

	void sendAuthCode(String email);
	VerifyAuthCodeResponseDTO verifyAuthCode(String email, String authCode);
	void deleteAuthCode(String email);
}
