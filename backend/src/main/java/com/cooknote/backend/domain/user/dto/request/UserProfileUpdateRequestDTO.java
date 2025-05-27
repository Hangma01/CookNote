package com.cooknote.backend.domain.user.dto.request;

import org.hibernate.validator.constraints.Length;

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
public class UserProfileUpdateRequestDTO {
	@NotBlank
	@Pattern(regexp = "^(?!\\s)(.{1,15})(?<!\\s)$")
	private String newNickname;
	
	@NotBlank()
	@Length(max = 400)
	private String newProfileImage;
}
