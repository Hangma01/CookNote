package com.cooknote.backend.global.infra.mail.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthCodeRequestDTO {

	private String email;
}
