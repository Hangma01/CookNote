package com.cooknote.backend.global.infra.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class VerifyAuthCodeResponseDTO {
	private boolean result;
	private String message;
}
