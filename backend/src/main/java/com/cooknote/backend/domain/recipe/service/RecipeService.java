package com.cooknote.backend.domain.recipe.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.recipe.dto.request.RecipeSaveRequestDTO;
import com.cooknote.backend.domain.recipe.dto.request.RecipeUpdateRequestDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeBookmarkResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeCardResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeDetailResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeLikeResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePrivateResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePublicResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeRecommnetResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeSearchResponseDTO;
import com.cooknote.backend.domain.recipe.enums.ConditionalType;

import jakarta.validation.Valid;

@Service
public interface RecipeService {
	
	// 레시피 상세 조회
	RecipeDetailResponseDTO getRecipeDetail(Long userId, Long recipeId);
	
	// 레시피 저장
	void recipeSave(Long userId, RecipeSaveRequestDTO saveRecipeRequestDTO);

	// 레시피 수정 조회
	RecipeEditResponseDTO getRecipeForEdit(Long userId, Long recipeId);

	// 레시피 수정
	void recipeUpdate(Long userId, @Valid RecipeUpdateRequestDTO recipeUpdateRequestDTO);

	// 레시피 삭제
	void recipeDelete(Long userId, Long recipeId);

	// 레시피 좋아요 생성
	void recipeLikeInsert(Long userId, Long recipeId);

	// 레시피 좋아요 삭제
	void recipeLikeDelete(Long userId, Long recipeId);

	// 레시피 북마크 추가
	void recipeBookmarkInsert(Long userId, Long recipeId);

	// 레시피 북마크 삭제
	void recipeBookmarkDelete(Long userId, Long recipeId);

	// 공개 레시피 조회
	Page<RecipePublicResponseDTO> getRecipePublic(Long accountId, int page, int size);
	
	// 비공개 레시피 조회
	Page<RecipePrivateResponseDTO> getRecipePrivate(Long userId, int page, int size);

	// 좋아요한 공개 레시피 조회
	Page<RecipeLikeResponseDTO> getLikeRecipe(Long userId, int page, int size);	
	
	// 북마크한 공개 레시피 조회
	Page<RecipeBookmarkResponseDTO> getBookmarkRecipe(Long userId, int page, int size);

	// 레시피 검색
	Page<RecipeSearchResponseDTO> getRecipeSearch(String keyword, int categoryCuisineId, int categoryPurposeId, int page, int size, ConditionalType conditionalType);

	// 레시피 재료 검색
	Page<RecipeSearchResponseDTO> getIngredientSearch(String keyword, int page, int size, ConditionalType conditionalType);

	// 팔로우한 유저들의 게시글 가져오기
	Page<RecipeSearchResponseDTO> getRecipesOfFollowingUsers(Long userId, int page, int size);

	// 팔로우한 특정 유저 게시글 가져오기
	Page<RecipeSearchResponseDTO> getRecipesByFollowingUser(Long userId, Long followingId, int page, int size);

	// 레시피 좋아요 갯수 가져오기
	Integer getRecipeLikeCount(Long recipeId);

	// 추천 레시피 가져오기
	List<RecipeRecommnetResponseDTO> getRecommentRecipe();
	
	// 베스트 레시피 가져오기
	List<RecipeCardResponseDTO> getBestRecipe();
	
	// 혼밥 레시피 가져오기
	List<RecipeRecommnetResponseDTO> getSoloRecipe();

	// 최신 레시피 가져오기
	List<RecipeCardResponseDTO> getRecentRecipe();






}
