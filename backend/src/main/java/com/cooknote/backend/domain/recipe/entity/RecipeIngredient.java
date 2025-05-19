package com.cooknote.backend.domain.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RecipeIngredient {
	private Long recipeIngredientId;  		// 레시피 순서 Key
    private int step;
    private String description;
    private String image;
}
