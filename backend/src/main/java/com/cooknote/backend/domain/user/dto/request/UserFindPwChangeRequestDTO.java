package com.cooknote.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFindPwChangeRequestDTO {

	private String changePwToken;
	
	@NotBlank()
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?])\\S{8,16}$")
	private String changePw;
	
	@NotBlank()
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?])\\S{8,16}$")
	private String changePwCheck;
}
