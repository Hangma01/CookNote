package com.cooknote.backend.domain.mail.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAuthCodeRequestDTO {
	private String email;
}
