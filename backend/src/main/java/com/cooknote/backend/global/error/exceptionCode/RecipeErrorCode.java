package com.cooknote.backend.global.error.exceptionCode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecipeErrorCode {

	RECIPE_IS_PRIVATE(HttpStatus.BAD_REQUEST, "작성자가 비공개한 레시피입니다."),
	RECIPE_IS_PRIVATE_ADMIN(HttpStatus.BAD_REQUEST, "관리자에 의해 삭제된 레피시입니다."),
	RECIPE_SELF_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "본인 레시피에는 좋아요를 할 수 없습니다."),
	RECIPE_INGREDIENT_EMPTY_EXCEPTION(HttpStatus.BAD_REQUEST, "찾고자 하는 재료를 입력해주세요.");
	
	private final HttpStatus httpStatus;
	private final String message;
}
