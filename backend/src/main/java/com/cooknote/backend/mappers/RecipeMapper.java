package com.cooknote.backend.mappers;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.recipe.dto.response.RecipeDetailResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.entity.Recipe;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;


@Mapper
public interface RecipeMapper {

	// 레시피 상세 조회
	RecipeDetailResponseDTO getRecipeDetail(@Param("userId") Long userId
										  , @Param("recipeId") Long recipeId);
	
	// 레시피 저장
	void save(Recipe reqRecipe);

	// 레시피 수정 - 데이터 요청
	RecipeEditResponseDTO getRecipeForEdit(@Param("userId") Long userId
										 , @Param("recipeId") Long recipeId);
	// 레시피 수정 - 업데이트
	void update(Recipe reqRecipe);

	// 레시피 삭제
	void recipeDelete(@Param("userId") Long userId
			 		, @Param("recipeId") Long recipeId
			 		, @Param("statusDelete") RecipeStatus statusDelete);

	// 레시피 좋아요 생성
	void recipeLikeInsert(@Param("userId") Long userId
			 			, @Param("recipeId") Long recipeId);
	
	// 레시피 좋아요 삭제
	void recipeLikeDelete(@Param("userId") Long userId
						, @Param("recipeId") Long recipeId);
	
	// 레시피 북마크 생성
	void recipeBookmarkInsert(@Param("userId") Long userId
							, @Param("recipeId") Long recipeId);
	
	// 레시피 북마크 삭제
	void recipeBookmarkDelete(@Param("userId") Long userId
			 				, @Param("recipeId") Long recipeId);

	// 레시피 존재 확인
	Recipe checkRecipe(@Param("recipeId") Long recipeId
			  		 , @Param("statusPublic") RecipeStatus statusPublic);
 
	// 유저 본인 레시피 인지 확인
	boolean checkSelfRecipeExists(@Param("userId") Long userId
							    , @Param("recipeId") Long recipeId
							    , @Param("statusPublic") RecipeStatus statusPublic
							    , @Param("statusPrivate") RecipeStatus statusPrivate);
}
