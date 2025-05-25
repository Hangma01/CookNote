package com.cooknote.backend.mappers;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.recipe.dto.response.RecipeBookmarkResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeDetailResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeLikeResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePrivateResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePublicResponseDTO;
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
			 		, @Param("recipeId") Long recipeId);

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

	// 공개 레시피 조회
	List<RecipePublicResponseDTO> getRecipePublic(@Param("accountId") Long accountId
												, @Param("size") int size
												, @Param("offset") int offset 
												, @Param("statusPublic") RecipeStatus statusPublic);

	// 비공개 레시피 조회
	List<RecipePrivateResponseDTO> getRecipePrivate(@Param("userId") Long userId
										     	  , @Param("size") int size
												  , @Param("offset") int offset 
												  , @Param("statusPrivate") RecipeStatus statusPrivate
												  , @Param("statusPrivateAdmin") RecipeStatus statusPrivateAdmin);
	
	// 좋아요한 레시피 조회
	List<RecipeLikeResponseDTO> getRecipeLike(@Param("userId") Long userId
											, @Param("size") int size
											, @Param("offset") int offset);

	// 북마크한 레시피 조회
	List<RecipeBookmarkResponseDTO> getRecipeBookmark(@Param("userId") Long userId
													, @Param("size") int size
													, @Param("offset") int offset);

	
	// 공개 레시피 토탈 갯수
	int recipesPublicCount(@Param("accountId") Long accountId
						 , @Param("statusPublic") RecipeStatus statusPublic);

	// 비공개 레시피 토탈 갯수
	int recipesPrivateCount(@Param("userId") Long userId
						  , @Param("statusPrivate") RecipeStatus statusPrivate
						  , @Param("statusPrivateAdmin") RecipeStatus statusPrivateAdmin);

	// 좋아요한 레시피 토탈 갯수
	int recipeLikeCount(@Param("userId") Long userId);
	
	// 북마크한 레시피 토탈 갯수 
	int recipeBookmarkCount(@Param("userId") Long userId);

	// 삭제할 이미지 가져오기
	Recipe getRecipeDeleteImage(@Param("userId") Long userId
							  , @Param("recipeId") Long recipeId);
}
