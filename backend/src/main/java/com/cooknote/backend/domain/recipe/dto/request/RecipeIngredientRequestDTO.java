package com.cooknote.backend.domain.recipe.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecipeIngredientRequestDTO {
	
	@NotBlank
	@Length(max = 20)
    private String name;
	
	@NotBlank
	@Length(max = 10)
	private String quantity;
    
	@Length(max = 20)
	private String remark;
}
