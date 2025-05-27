package com.cooknote.backend.domain.user.service;

import com.cooknote.backend.domain.user.dto.request.UserProfileUpdateRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserPwEditRequestDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileEditInfoResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserFollowResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserHostProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.cooknote.backend.domain.user.entity.User;

import jakarta.validation.Valid;

public interface UserService {

	User getLoginUser(String id);

	// 호스트 프로필 조회
	UserHostProfileResponseDTO getHostProfile(Long userId, Long hostId);

	// 프로필 조회
	UserProfileResponseDTO getProfile(Long userId);

	// 팔로우 조회
	UserFollowResponseDTO getFollow(Long userId);

	// 팔로우 생성
	void insertFollow(Long userId, Long followId);

	// 팔로우 취소
	void deleteFollow(Long userId, Long followId);
	
	// 유저 프로필 수정 정보 조회
	UserProfileEditInfoResponseDTO getUserProfileEditInfo(Long userId);

	// 유저 프로필 수정
	void userProfileUpdate(Long userId, UserProfileUpdateRequestDTO userProfileUpdateRequestDTO);
	
	// 유저 비밀번호 수정
	void userPwUpdate(Long userId, @Valid UserPwEditRequestDTO userPwEditRequestDTO);

	// 회원 탈퇴
	void userDelete(Long userId);

}
