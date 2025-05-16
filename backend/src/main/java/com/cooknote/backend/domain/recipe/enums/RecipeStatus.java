package com.cooknote.backend.domain.recipe.enums;

import lombok.Getter;

@Getter
public enum RecipeStatus {
    PRIVATE("비공개"),				// 비공개
    PUBLIC("공개"),					// 공개
    PRIVATE_ADMIN("관리자 비공개"),		// 관리자 비공개
    DELETE("삭제");					// 삭제
    
    private final String label;

    RecipeStatus(String label) {
        this.label = label;
    }
}
