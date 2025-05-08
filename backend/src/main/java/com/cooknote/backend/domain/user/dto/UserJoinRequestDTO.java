package com.cooknote.backend.domain.user.dto;

import java.util.Date;

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
	@Pattern(regexp = "^[^\\s].{1,15}[^\\s]$")
	private String password;
	
	@NotBlank()
	@Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$")
	private String name;

	
	@NotBlank
	@Pattern(regexp = "^[^\\s].{1,15}[^\\s]$")
	private String nickname;

	@NotBlank()
	@Pattern(regexp = "^[^\\s][a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}[^\\s]$")
	private String email;
}
