package com.cooknote.backend.domain.user.enums;

import lombok.Getter;

@Getter
public enum ReportType {
	RECIPE("레시피"),		// 레시피 신고
	COMMENT("댓글");		// 댓글 신고
	
	private final String label;

	ReportType(String label) {
        this.label = label;
    }
}
