package com.cooknote.backend.domain.recipe.service;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.recipe.dto.request.RecipeSaveRequestDTO;
import com.cooknote.backend.domain.recipe.dto.request.RecipeUpdateRequestDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;

import jakarta.validation.Valid;

@Service
public interface RecipeService {
	public void recipeSave(long userId, RecipeSaveRequestDTO saveRecipeRequestDTO);

	public RecipeEditResponseDTO getRecipeForEdit(long userId, String recipeId);

	public void recipeUpdate(long userId, @Valid RecipeUpdateRequestDTO recipeUpdateRequestDTO);
}
