package com.cooknote.backend.domain.comment.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentUpdateRequestDTO {
	
	@NotNull
	private Long commentId;
	
	@NotBlank()
	@Length(max = 250)
	private String content;
}
