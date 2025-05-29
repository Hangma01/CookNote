package com.cooknote.backend.domain.category.enums;

import lombok.Getter;

@Getter
public enum CategoryPurposeEnum {
    DIET(1),
    SNACK(2),
    SOLO_MEAL(3),
    HIGH_PROTEIN(4),
    CAMPING(5),
    NUTRITION(6),
    SIDE_DISH(7),
    DESSERT(8),
    ETC(9);
	
	private final int code;

	CategoryPurposeEnum(int code) {
        this.code = code;
    }
}
