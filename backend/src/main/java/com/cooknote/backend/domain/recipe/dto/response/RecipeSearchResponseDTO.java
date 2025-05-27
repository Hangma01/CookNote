package com.cooknote.backend.domain.recipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecipeSearchResponseDTO {
	private Long recipeId;
	private String recipeThumbnail;
	private String recipeTitle;
	private Long writerId;
	private String writerNickname;
	private String writerProfileImage;
	private int recipeCommentCount;
	private int recipeLikeCount;
}
