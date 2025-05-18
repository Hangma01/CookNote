package com.cooknote.backend.domain.recipe.service;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.recipe.dto.request.RecipeSaveRequestDTO;

@Service
public interface RecipeService {
	public void recipeSave(long userId, RecipeSaveRequestDTO saveRecipeRequestDTO);
}
