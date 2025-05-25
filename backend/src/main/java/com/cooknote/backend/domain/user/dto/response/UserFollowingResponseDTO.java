package com.cooknote.backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserFollowingResponseDTO {
	private Long followingId;
	private String followingNickname;
	private String followingProfileImage;
}
