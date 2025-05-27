package com.cooknote.backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserFollowingLatestForRecipeResponseDTO {
	private Long followingId;
	private String nickname;
	private String profileImage;
}
