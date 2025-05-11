package com.cooknote.backend.domain.mail.dto.response;

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
