package com.cooknote.backend.global.infra.mail.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyAuthCodeRequestDTO {
	
	private String email;
	private String authCode;
}
