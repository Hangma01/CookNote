package com.cooknote.backend.mappers;


import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.recipe.dto.response.RecipeBookmarkResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeCardResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeDetailResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeEditResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeLikeResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePrivateAdminResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePrivateResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipePublicResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeRecommnetResponseDTO;
import com.cooknote.backend.domain.recipe.dto.response.RecipeSearchResponseDTO;
import com.cooknote.backend.domain.recipe.entity.Recipe;
import com.cooknote.backend.domain.recipe.enums.ConditionalType;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;


@Mapper
public interface RecipeMapper {

	// 레시피 상세 조회
	RecipeDetailResponseDTO getRecipeDetail(@Param("userId") Long userId
										  , @Param("recipeId") Long recipeId
										  , @Param("statusDelete") RecipeStatus statusDelete);
	
	// 레시피 저장
	void save(Recipe reqRecipe);

	// 레시피 수정 - 데이터 요청
	RecipeEditResponseDTO getRecipeForEdit(@Param("userId") Long userId
										 , @Param("recipeId") Long recipeId
										 , @Param("statusDelete") RecipeStatus statusDelete);
	// 레시피 수정 - 업데이트
	void update(@Param("reqRecipe") Recipe reqRecipe
			  , @Param("statusPrivateAdmin") RecipeStatus privateAdmin);

	// 레시피 삭제
	void recipeDelete(@Param("userId") Long userId
			 		, @Param("recipeId") Long recipeId
			 		, @Param("statusDelete") RecipeStatus statusDelete);
	
	// 레시피 순서 이미지 삭제
	void recipeSeqDelete(@Param("recipeId") Long recipeId);

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
												  , @Param("statusPrivate") RecipeStatus statusPrivate);
	
	// 관리자 비공개 레시피 조회
	List<RecipePrivateAdminResponseDTO> getRecipePrivateAdmin(@Param("userId") Long userId
										     	  	   		, @Param("size") int size
										     	  	   		, @Param("offset") int offset
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
						  , @Param("statusPrivate") RecipeStatus statusPrivate);
	
	// 관리자 비공개 레시피 토탈 갯수
	int recipesPrivateAdminCount(@Param("userId") Long userId
						  , @Param("statusPrivateAdmin") RecipeStatus statusPrivateAdmin);

	// 좋아요한 레시피 토탈 갯수
	int recipeLikeCount(@Param("userId") Long userId);
	
	// 북마크한 레시피 토탈 갯수 
	int recipeBookmarkCount(@Param("userId") Long userId);

	// 삭제할 이미지 가져오기
	LocalDateTime getRecipeCreateAt(@Param("userId") Long userId
						   		  , @Param("recipeId") Long recipeId);

	// 레시피 검색
	List<RecipeSearchResponseDTO> getRecipeSearch(@Param("keyword") String keyword
												, @Param("categoryCuisineId") int categoryCuisineId
												, @Param("categoryPurposeId") int categoryPurposeId
												, @Param("size") int size
												, @Param("offset") int offset
												, @Param("statusRecipePublic") RecipeStatus statusRecipePublic
												, @Param("conditionalType") ConditionalType conditionalType
												, @Param("conditionalPopular") ConditionalType popular
												, @Param("conditionalLatest") ConditionalType latest);

	// 레시피 검색 토탈 갯수
	int getRecipeSearchCount(@Param("keyword") String keyword
						   , @Param("categoryCuisineId") int categoryCuisineId
						   , @Param("categoryPurposeId") int categoryPurposeId
						   , @Param("statusRecipePublic") RecipeStatus statusRecipePublic);

	// 레시피 재료 검색	
	List<RecipeSearchResponseDTO> getIngredientSearch(@Param("ingredients") List<String> ingredients
													, @Param("ingredientCount") int ingredientCount
													, @Param("size") int size
													, @Param("offset") int offset
													, @Param("statusRecipePublic") RecipeStatus statusRecipePublic
													, @Param("conditionalType") ConditionalType conditionalType
													, @Param("conditionalPopular") ConditionalType popular
													, @Param("conditionalLatest") ConditionalType latest);
	// 레시피 재료 검색 토탈 갯수
	int getIngredientSearchCount(@Param("ingredients") List<String> ingredients
						   	   , @Param("ingredientCount") int ingredientCount
						   	   , @Param("statusRecipePublic") RecipeStatus statusRecipePublic);

	// 유저가 팔로우한 유저들의 레시피
	List<RecipeSearchResponseDTO> getRecipesOfFollowingUsers(@Param("userId") Long userId
														   , @Param("size") int size
														   , @Param("offset") int offset
														   , @Param("statusRecipePublic") RecipeStatus statusRecipePublic);

	// 유저가 팔로우한 유저들의 레시피 토탈 갯수
	int getRecipesOfFollowingUsersCount(@Param("userId") Long userId
									  , @Param("statusRecipePublic") RecipeStatus statusRecipePublic);

	// 유저가 팔오우한 유저의 특정 레시피
	List<RecipeSearchResponseDTO> getRecipesByFollowingUser(@Param("userId") Long userId
														   , @Param("followingId") Long followingId
														   , @Param("size") int size
														   , @Param("offset") int offset
														   , @Param("statusRecipePublic") RecipeStatus statusRecipePublic);

	// 유저가 팔로우한 유저의 특정 레시피 토탈 갯수
	int getRecipesByFollowingUserCount(@Param("userId") Long userId
									 , @Param("followingId") Long followingId
									 , @Param("statusRecipePublic") RecipeStatus statusRecipePublic);

	// 레시피 좋아요한 카운트 가져오기
	int getRecipeLikeCount(@Param("recipeId") Long recipeId);

	// 회원 탈퇴 시 레시피 삭제
	void recipeDeleteByUserId(@Param("userId")Long userId
							, @Param("statusRecipeDelete") RecipeStatus statusRecipeDelete);

	
	// 회원 탈퇴 시 레시피 순서 삭제
	void recipeSeqDeleteByUserId(@Param("userId") Long userId);

	// 추천 레시피 가져오기
	List<RecipeRecommnetResponseDTO> getRecipeRecommnet(@Param("statusRecipePublic") RecipeStatus statusRecipePublic
													  , @Param("size") int getRecommentRecipeLimit);

	List<RecipeCardResponseDTO> getSoloMealRecipe(@Param("statusRecipePublic") RecipeStatus statusRecipePublic
												, @Param("categoryPurposeSoloMeal") int categoryPurposeSoloMeal
												, @Param("size") int getSoloRecipeLimit);
}
