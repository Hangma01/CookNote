package com.cooknote.backend.domain.recipe.entity;

import java.time.LocalDate;

import com.cooknote.backend.domain.recipe.enums.RecipeStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeSeq {
	private Long recipeSeqId;  		// 레시피 순서 Key
	private Long recipeId;     		// 레시피 Key
	private String description;  	// 레시피 순서별 설명
	private String image; 			// 레시피 순서별 이미지
}
