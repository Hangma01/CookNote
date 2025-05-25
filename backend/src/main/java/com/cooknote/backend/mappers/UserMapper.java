package com.cooknote.backend.mappers;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.user.dto.response.UserHostProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.cooknote.backend.domain.user.entity.Follow;
import com.cooknote.backend.domain.user.entity.User;


@Mapper
public interface UserMapper {
	
	// 회원 정보 가져오기
	User getUser(@Param("id") String id);

	// 유저 호스트 프로필 정보 가져오기	
	UserHostProfileResponseDTO getHostProfile(@Param("userId") Long userId
									, @Param("hostId") Long hostId);

	// 유저 본인 프로필 정보 가져오기	
	UserProfileResponseDTO getProfile(@Param("userId") Long userId);

	// 팔로워한 유저 정보 가졍괴	
	List<Follow> getFollower(@Param("userId") Long userId);

	// 팔로잉한 유저 정보 가져오기	
	List<Follow> getFollowing(@Param("userId") Long userId);

	// 유저 존재 확인	
	boolean existsUser(@Param("userId") Long userId);

	// 팔로우 하기
	void insertFollow(@Param("userId")Long userId
					, @Param("followId") Long followId);

	
	// 팔로우 취소
	void deleteFollow(@Param("userId") Long userId
			   		, @Param("followId") Long followId);
}
