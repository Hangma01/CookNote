package com.cooknote.backend.domain.recipe.dto.response;


import java.util.List;

import com.cooknote.backend.domain.category.dto.response.CategoryGetAllResponseDTO;
import com.cooknote.backend.domain.recipe.enums.RecipeDuration;
import com.cooknote.backend.domain.recipe.enums.RecipeLevel;
import com.cooknote.backend.domain.recipe.enums.RecipeServing;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeEditResponseDTO {
    private String title;				// 레시피 제목
    private String description;			// 레시피 요리소개
    private String thumbnail;			// 레시피 썸네일
    private String videoId;				// 동영상 아이디
    private RecipeServing serving;		// 레시피 인원수
    private RecipeDuration duration;	// 레시피 요리시간
    private RecipeLevel level;	 		// 레시피 난이도 - 0 (쉬움), 1 (보통), 3 (어려움)
    private String tip;					// 레시피 요리 팁
    private RecipeStatus status; 		// 레시피 상태 - PRIVATE, PUBLIC, PRIVATE_ADMIN, ELETE.
    private int categoryCuisineId;		// 카테고리 - 요리 종류 (FK)
    private int categoryPurposeId;		// 카테고리 - 요리 목적 (FK)
    
    private List<RecipeIngredientResponseDTO> recipeIngredients;
    private List<RecipeSeqResponseDTO> recipeSeqs;

}
