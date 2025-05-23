package com.cooknote.backend.domain.recipe.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeBookmarkRequestDTO {
	 
    @NotNull
    private Long recipeId;
}
