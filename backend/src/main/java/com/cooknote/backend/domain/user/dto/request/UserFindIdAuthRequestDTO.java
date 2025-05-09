package com.cooknote.backend.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFindIdAuthRequestDTO {
	private String name;
	private String email;
	private String authCode;
}
