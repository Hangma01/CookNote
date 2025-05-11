package com.cooknote.backend.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFindPwAuthRequestDTO {
	private String userId;
	private String email;
}
