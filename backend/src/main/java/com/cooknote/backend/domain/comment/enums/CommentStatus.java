package com.cooknote.backend.domain.comment.enums;

import lombok.Getter;

@Getter
public enum CommentStatus {
    PUBLIC("공개"),					// 공개
    PRIVATE_ADMIN("관리자 비공개"),		// 관리자 비공개
    DELETE("삭제");					// 삭제
    
    private final String label;

  
    CommentStatus(String label) {
        this.label = label;
    }
}
