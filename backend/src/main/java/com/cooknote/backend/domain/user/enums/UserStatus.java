package com.cooknote.backend.domain.user.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
	ACTIVE,		// 활성화
	SUSPEND,	// 정지
	DELETE,		// 회원탈퇴
}
