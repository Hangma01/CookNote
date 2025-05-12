package com.cooknote.backend.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequestDTO {
	
	@NotBlank()
	@Pattern(regexp = "^[a-z0-9]{5,20}$")
	private String userId;
	
	@NotBlank()
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?])\\S{8,16}$")
	private String pw;
	
	@NotBlank()
	@Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$")
	private String name;

	
	@NotBlank
	@Pattern(regexp = "^(?!\\s)(.{1,15})(?<!\\s)$")
	private String nickname;

	@NotBlank()
	@Pattern(regexp = "^[^\\s][a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,}[^\\s]$")
	private String email;
}
