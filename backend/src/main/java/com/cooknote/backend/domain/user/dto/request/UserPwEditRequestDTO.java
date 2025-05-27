package com.cooknote.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPwEditRequestDTO {
	
	@NotBlank()
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?])\\S{8,16}$")
	private String currentPw;
	
	@NotBlank()
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?])\\S{8,16}$")
	private String newPw;
	
	@NotBlank()
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?])\\S{8,16}$")
	private String newPwConfirm;
}
