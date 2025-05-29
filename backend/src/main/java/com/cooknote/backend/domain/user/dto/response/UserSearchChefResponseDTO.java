package com.cooknote.backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchChefResponseDTO {
	private Long userId;
	private String nickname;
	private String profileImage;
	private int followerCount;
	private int recipeCount;
	private int likeCount;
	private int bookmarkCount;
	private boolean isFollowing;
}
