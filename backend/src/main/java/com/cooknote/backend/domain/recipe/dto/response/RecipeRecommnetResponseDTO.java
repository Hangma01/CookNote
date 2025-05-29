package com.cooknote.backend.domain.recipe.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRecommnetResponseDTO {
	private String recipeThumbnail;
	private Long recipeId;
	private String recipeTitle;
}
