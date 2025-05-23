package com.cooknote.backend.global.error.exceptionCode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecipeErrorCode {

	RECIPE_IS_DELETE(HttpStatus.BAD_REQUEST, "작성자에 의해 삭제된 레시피입니다."),
	RECIPE_IS_PRIVATE_ADMIN(HttpStatus.BAD_REQUEST, "관리자에 의해 삭제된 레피시입니다."),
	RECIPE_SELF_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "본인 레시피에는 좋아요, 북마크, 신고, 댓글을 하실수 없습니다.");
	
	private final HttpStatus httpStatus;
	private final String message;
}
