package com.cooknote.backend.domain.recipe.entity;

import java.time.LocalDate;

import com.cooknote.backend.domain.recipe.enums.RecipeStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredient {
	private Long recipeIngredientId;  		// 레시피 순서 Key
    private int step;
    private String description;
    private String image;
}
