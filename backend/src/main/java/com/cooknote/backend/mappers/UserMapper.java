package com.cooknote.backend.mappers;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.user.dto.response.UserProfileEditInfoResponseDTO;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;
import com.cooknote.backend.domain.user.dto.response.UserFollowingLatestForRecipeResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserHostProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.cooknote.backend.domain.user.entity.Follow;
import com.cooknote.backend.domain.user.entity.User;


@Mapper
public interface UserMapper {
	
	// 로그인 회원 정보 가져오기
	User getLoginUser(@Param("id") String id);
	
	// 회원 정보 가져오기
	User getUser(@Param("userId") Long userId);

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

	// 프로필 수정
	void userProfileUpdate(@Param("userId") Long userId
			      	  	 , @Param("updateNickname") String nickname 
			      	  	 , @Param("moveProfileUrl") String moveProfileUrl);

	// 유저 수정 정보 조회
	UserProfileEditInfoResponseDTO getUserProfileEditInfo(@Param("userId") Long userId);

	// 비밀번호 수정
	void pwUpdate(@Param("userId") Long userId
				, @Param("newPw") String newPw);

	// 회원 탈퇴
	void userDelete(@Param("userId") Long userId);

	// 팔로잉한 유저중 레시피 작성 최신순
	List<UserProfileEditInfoResponseDTO> getFollowingLatestForRecipe(@Param("userId") Long userId
							   	   								   , @Param("statusRecipePublic") RecipeStatus statusRecipePublic);
	



}
