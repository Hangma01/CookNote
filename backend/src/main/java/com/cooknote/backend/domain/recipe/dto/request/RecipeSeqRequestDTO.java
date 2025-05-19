package com.cooknote.backend.domain.recipe.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class RecipeSeqRequestDTO {
	
	@NotNull
    private Integer step;
	
	@NotBlank
	@Length(min = 10, max = 250)
    private String description;
	
	@Length(max = 400)
    private String image;
}
