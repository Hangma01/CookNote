package com.cooknote.backend.domain.recipe.enums;

import lombok.Getter;

@Getter
public enum RecipeServing {
	ONE("1인분"),
	TWO("2인분"),
	THREE("3인분"),
	OVER_FOUR("4인분 이상");
	
    private final String label;

	RecipeServing(String label) {
        this.label = label;
    }
}
