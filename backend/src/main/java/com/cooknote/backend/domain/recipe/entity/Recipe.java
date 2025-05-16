package com.cooknote.backend.domain.recipe.entity;

import java.time.LocalDate;

import com.cooknote.backend.domain.recipe.enums.RecipeLevel;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {
    private Long recipeId;				// 레시피 Key
    private String title;				// 레시피 제목
    private String description;			// 레시피 요리소개
    private String thumbnail;			// 레시피 썸네일
    private String videoId;				// 동영상 아이디
    private int serving;				// 레시피 인원수
    private int duration;				// 레시피 요리시간
    private RecipeLevel level;	 		// 레시피 난이도 - EASY, NORMAL, HARD
    private String tip;					// 레시피 요리 팁
    private RecipeStatus status; 		// 레시피 상태 - PRIVATE, PUBLIC, PRIVATE_ADMIN, ELETE.
    private LocalDate createAt;			// 레시피 생성일
    private LocalDate updateAt;			// 레시피 수정일
    private int categoryCuisineId;		// 카테고리 - 요리 종류 (FK)
    private int categoryPurposeId;		// 카테고리 - 요리 목적 (FK)
    private Long writerId;				// 작성자 (FK)
}
