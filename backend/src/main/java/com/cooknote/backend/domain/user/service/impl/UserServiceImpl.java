package com.cooknote.backend.domain.user.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.user.dto.response.UserFollowResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserHostProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.cooknote.backend.domain.user.entity.Follow;
import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserMapper userMapper;
	
	// 유저 조회
	@Override
	public User getUser(String id) {

		return userMapper.getUser(id);
	}

	// 유저 프로필 정보 조회
	@Override
	public UserHostProfileResponseDTO getHostProfile(Long userId, Long hostId) {
		
		return userMapper.getHostProfile(userId, hostId);
		
	}

	// 본인 프로필 정보 조회
	@Override
	public UserProfileResponseDTO getProfile(Long userId) {

		return userMapper.getProfile(userId);
	}

	// 유저 팔로우 조회
	@Override
	public UserFollowResponseDTO getFollow(Long userId) {
		
		List<Follow> getFollower = userMapper.getFollower(userId);
		
		List<Follow> getFollowing = userMapper.getFollowing(userId);
		
		UserFollowResponseDTO userFollowResponseDTO = UserFollowResponseDTO.builder()
																			.follower(getFollower)
																			.following(getFollowing)
																			.build();
		
		return userFollowResponseDTO;
	}

	// 유저 팔로우 하기
	@Override
	public void insertFollow(Long userId, Long followId) {
		
		if(!userMapper.existsUser(followId)) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}	
		
		userMapper.insertFollow(userId, followId);
	}

	// 유저 팔로우 취소
	@Override
	public void deleteFollow(Long userId, Long followId) {

		if(!userMapper.existsUser(followId)) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}	
		
		userMapper.deleteFollow(userId, followId);
	}
	
	
}
