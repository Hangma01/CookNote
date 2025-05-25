package com.cooknote.backend.domain.user.dto.response;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserFollowerResponseDTO {
	private Long followerId;
	private String followerNickname;
	private String followerProfileImage;
	private boolean isFollowingBack;
}
