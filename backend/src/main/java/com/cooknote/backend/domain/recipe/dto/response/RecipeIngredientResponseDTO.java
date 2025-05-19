package com.cooknote.backend.domain.recipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientResponseDTO {
    private String name;
	
	private String quantity;
    
	private String remark;
}