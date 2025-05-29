package com.cooknote.backend.domain.recipe.dto.response;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipePrivateResponseDTO {
	private Long recipeId;				// 레시피 아이디
	private String title;				// 레시피 제목
    private String description;			// 레시피 요리소개
    private String thumbnail;			// 레시피 썸네일
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDateTime createAt;		// 생성 날짜
}
