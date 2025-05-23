package com.cooknote.backend.global.error.excption;

import com.cooknote.backend.global.error.exceptionCode.RecipeErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomRecipeException extends RuntimeException {
	private final RecipeErrorCode recipeError;
}
