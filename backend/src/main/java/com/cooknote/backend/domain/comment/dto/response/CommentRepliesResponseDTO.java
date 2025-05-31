package com.cooknote.backend.domain.comment.dto.response;

import java.time.LocalDateTime;
import java.util.Date;

import com.cooknote.backend.domain.comment.enums.CommentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRepliesResponseDTO {
	private Long commentId;
	private Long writerId;
	private String content;
	private LocalDateTime createAt;
	private String nickname;
	private String profileImage;
}
