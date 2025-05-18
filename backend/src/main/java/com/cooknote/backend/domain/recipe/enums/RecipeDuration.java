package com.cooknote.backend.domain.recipe.enums;

import lombok.Getter;

@Getter
public enum RecipeDuration {
	WITHIN_30("30분 이내"),
	WITHIN_60("1시간 이내"),
	WITHIN_120("2시간 이내"),
	OVER_120("2시간 이상");	
    
	private final String label;

	RecipeDuration(String label) {
        this.label = label;
    }
}
