package com.cooknote.backend.domain.user.dto.response;

import java.util.List;

import com.cooknote.backend.domain.user.entity.Follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserFollowResponseDTO {
	private List<Follow> following;
	private List<Follow> follower;
}
