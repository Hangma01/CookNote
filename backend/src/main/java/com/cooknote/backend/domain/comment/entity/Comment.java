package com.cooknote.backend.domain.comment.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.cooknote.backend.domain.comment.enums.CommentStatus;

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
	private String content;
	private CommentStatus status;
	private LocalDateTime createAt;
	private LocalDateTime updateAt;
	private Long recipeId;
	private Long writerId;
	private Long parentCommentId;
}
