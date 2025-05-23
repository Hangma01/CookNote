package com.cooknote.backend.domain.recipe.service;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.recipe.dto.request.RecipeSaveRequestDTO;
import com.cooknote.backend.domain.recipe.dto.request.RecipeUpdateRequestDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeDetailResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;

import jakarta.validation.Valid;

@Service
public interface RecipeService {
	
	// 레시피 상세 조회
	public RecipeDetailResponseDTO getRecipeDetail(Long userId, Long recipeId);
	
	
	// 레시피 저장
	public void recipeSave(Long userId, RecipeSaveRequestDTO saveRecipeRequestDTO);

	// 레시피 수정 조회
	public RecipeEditResponseDTO getRecipeForEdit(Long userId, Long recipeId);

	// 레시피 수정
	public void recipeUpdate(Long userId, @Valid RecipeUpdateRequestDTO recipeUpdateRequestDTO);


	public void recipeDelete(Long userId, Long recipeId);


	void recipeLikeInsert(Long userId, Long recipeId);


	void recipeLikeDelete(Long userId, Long recipeId);


	void recipeBookmarkInsert(Long userId, Long recipeId);


	void recipeBookmarkDelete(Long userId, Long recipeId);

	
}
