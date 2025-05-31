package com.cooknote.backend.domain.user.enums;

import lombok.Getter;

@Getter
public enum ReportStatus {
	PENDING("처리 진행중"),	// 처리 진행중
	REJECTED("반려됨"),		// 거부
	APPROVED("제제 처리됨");	// 승인
	
	private final String label;

	ReportStatus(String label) {
        this.label = label;
    }
}
