package com.cooknote.backend.domain.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentInsertRequestDTO {
	private String content;
	private Long RecipeId;
	private Long WriterId;
	private Long ParentCommentId;
}
