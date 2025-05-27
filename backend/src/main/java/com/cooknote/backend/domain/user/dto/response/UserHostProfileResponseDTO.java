package com.cooknote.backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserHostProfileResponseDTO {
	private String hostId;
	private String nickname;
	private String profileImage;
	private int recipePublicCount;
	private int recipePrivateCount;
	private int followingCount;
	private int followerCount;
	private boolean isFollow;
}
