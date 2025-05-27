package com.cooknote.backend.domain.recipe.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class RecipeSeqRequestDTO {
	
	@NotNull
    private Integer step;
	
	@NotBlank
	@Length(min = 5, max = 400)
    private String description;
	
	@NotBlank
	@Length(max = 400)
    private String image;
}
