package com.cooknote.backend.domain.user.service;

import com.cooknote.backend.domain.user.dto.response.UserFollowResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserHostProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.cooknote.backend.domain.user.entity.User;

public interface UserService {

	User getUser(String id);

	UserHostProfileResponseDTO getHostProfile(Long userId, Long hostId);

	UserProfileResponseDTO getProfile(Long userId);

	UserFollowResponseDTO getFollow(Long userId);

	void insertFollow(Long userId, Long followId);

	void deleteFollow(Long userId, Long followId);

}
