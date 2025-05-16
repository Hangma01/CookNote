package com.cooknote.backend.domain.recipe.enums;

import lombok.Getter;

@Getter
public enum RecipeLevel {
    EASY("쉬움"),
    NORMAL("보통"),
    HARD("어려움");

    private final String label;

    RecipeLevel(String label) {
        this.label = label;
    }
}
