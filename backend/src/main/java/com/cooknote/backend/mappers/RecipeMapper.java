package com.cooknote.backend.mappers;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.recipe.dto.response.RecipeDetailResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.entity.Recipe;


@Mapper
public interface RecipeMapper {

	// 레시피 상세 조회
	RecipeDetailResponseDTO getRecipeDetail(String recipeId);
	
	// 레시피 저장
	void save(Recipe reqRecipe);

	// 레시피 수정 - 데이터 요청
	RecipeEditResponseDTO getRecipeForEdit(@Param("recipeId") String recipeId
										 		, @Param("userId") Long userId);
	// 레시피 수정 - 업데이트
	void update(Recipe reqRecipe);


}
