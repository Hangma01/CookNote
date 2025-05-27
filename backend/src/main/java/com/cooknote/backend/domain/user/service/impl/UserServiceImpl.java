package com.cooknote.backend.domain.user.service.impl;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooknote.backend.domain.recipe.enums.RecipeStatus;
import com.cooknote.backend.domain.user.dto.request.UserProfileUpdateRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserPwEditRequestDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileEditInfoResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserFollowResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserFollowingLatestForRecipeResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserHostProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.cooknote.backend.domain.user.entity.Follow;
import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.exceptionCode.UserErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.error.excption.CustomUserException;
import com.cooknote.backend.global.infra.aws.s3.service.S3Service;
import com.cooknote.backend.global.utils.common.CommonFunctionUtil;
import com.cooknote.backend.mappers.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserMapper userMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final S3Service s3Service;	
	
	// 유저 조회
	@Override
	public User getLoginUser(String id) {

		return userMapper.getLoginUser(id);
	}

	// 유저 프로필 정보 조회
	@Override
	public UserHostProfileResponseDTO getHostProfile(Long userId, Long hostId) {
		
		User host = userMapper.getUser(hostId);
		
		if(host == null) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
		
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
	@Transactional
	public void insertFollow(Long userId, Long followId) {
		
		if(!userMapper.existsUser(followId)) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}	
		
		userMapper.insertFollow(userId, followId);
	}

	// 유저 팔로우 취소
	@Override
	@Transactional
	public void deleteFollow(Long userId, Long followId) {

		if(!userMapper.existsUser(followId)) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}	
		
		userMapper.deleteFollow(userId, followId);
	}

	// 프로필 업데이트
	@Override
	@Transactional
	public void userProfileUpdate(Long userId, UserProfileUpdateRequestDTO userProfileUpdateRequestDTO) {
		
		User user = userMapper.getUser(userId);
		
		if(user == null) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}

		String newNickname = userProfileUpdateRequestDTO.getNewNickname();
		String newProfileImage = userProfileUpdateRequestDTO.getNewProfileImage();
		
		String updateNickname = null;
		String moveProfileUrl = null;
		
		if(!CommonFunctionUtil.match(newNickname, user.getNickname())) {
			updateNickname = newNickname;
		}
		
		if(!CommonFunctionUtil.match(newProfileImage, user.getProfileImage())) {
			moveProfileUrl = s3Service.moveImage(newProfileImage, userId, Constans.S3_MOVE_USER_PROFILE_PATH, null);
			
			// 프로필 이미지가 default 이미지면 삭제 안하기
			s3Service.deleteImageFromS3(user.getProfileImage());
		}
		
		if(updateNickname == null && moveProfileUrl == null) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		userMapper.userProfileUpdate(userId, updateNickname, moveProfileUrl);
	}

	// 유저 프로필 정보 조회
	@Override
	public UserProfileEditInfoResponseDTO getUserProfileEditInfo(Long userId) {
		
		return userMapper.getUserProfileEditInfo(userId);
	}

	@Override
	@Transactional
	public void userPwUpdate(Long userId, @Valid UserPwEditRequestDTO userPwEditRequestDTO) {
		
		User user = userMapper.getUser(userId);
		
        // 기존 비밀번호가 일치하는지 확인
        if (!bCryptPasswordEncoder.matches(userPwEditRequestDTO.getCurrentPw(), user.getPassword())) {
            throw new CustomCommonException(CommonErrorCode.INPUT_MATCH_EXCEPTIOON);
        }
        
        // 새로운 비밀번호 및 확인용 비밀번호 일치하는지 체크
        if (!CommonFunctionUtil.match(userPwEditRequestDTO.getNewPw()
        						    , userPwEditRequestDTO.getNewPwConfirm())) {
        	throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
        }

        // 기존 비밀번호와 새로운 비밀번호가 일치하는지 체크
        if (bCryptPasswordEncoder.matches(userPwEditRequestDTO.getNewPw(), user.getPassword())) {
        	throw new CustomUserException(UserErrorCode.PW_UPDATE_MATCH_EXCEPTION);
        }
       
        String newPw = bCryptPasswordEncoder.encode(userPwEditRequestDTO.getNewPw());
        userMapper.pwUpdate(userId, newPw);
	}

	// 회원 탈퇴
	@Override
	@Transactional
	public void userDelete(Long userId) {
		userMapper.userDelete(userId);
	}

	// 팔로잉 게시글 작성 최신순으로 가져오기 
	@Override
	public List<UserProfileEditInfoResponseDTO> getFollowingLatestForRecipe(Long userId) {
		
		
		return userMapper.getFollowingLatestForRecipe(userId, RecipeStatus.PUBLIC);
		
		
	}
}
