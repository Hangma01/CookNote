package com.cooknote.backend.domain.comment.entity;

import java.time.LocalDate;
import java.util.Date;

import com.cooknote.backend.domain.comment.enums.CommentStatus;
import com.cooknote.backend.domain.recipe.entity.Recipe;
import com.cooknote.backend.domain.recipe.enums.RecipeDuration;
import com.cooknote.backend.domain.recipe.enums.RecipeLevel;
import com.cooknote.backend.domain.recipe.enums.RecipeServing;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Comment {
	
	private Long commentId;
	private String Content;
	private CommentStatus status;
	private Date createAt;
	private Date updateAt;
	private Long RecipeId;
	private Long WriterId;
	private Long ParentCommentId;
}
