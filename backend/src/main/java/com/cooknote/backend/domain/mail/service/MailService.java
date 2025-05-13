package com.cooknote.backend.domain.mail.service;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.mail.dto.response.VerifyAuthCodeResponseDTO;

@Service
public interface MailService {

	void sendAuthCode(String email);
	VerifyAuthCodeResponseDTO verifyAuthCode(String email, String authCode);
	void deleteAuthCode(String email);
}
