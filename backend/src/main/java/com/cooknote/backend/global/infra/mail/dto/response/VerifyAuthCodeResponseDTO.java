package com.cooknote.backend.global.infra.mail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyAuthCodeResponseDTO {
	private boolean result;
	private String message;
}
