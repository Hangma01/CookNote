package com.cooknote.backend.domain.user.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooknote.backend.domain.comment.enums.CommentStatus;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;
import com.cooknote.backend.domain.user.dto.request.UserProfileUpdateRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserPwEditRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserReportDupliationCheckRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserReportInsertRequestDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileEditInfoResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserFollowResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserHostProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserReportResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserSacntionResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserSearchChefResponseDTO;
import com.cooknote.backend.domain.user.entity.Follow;
import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.domain.user.enums.ReportStatus;
import com.cooknote.backend.domain.user.enums.ReportType;
import com.cooknote.backend.domain.user.enums.UserStatus;
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.constants.Constans;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.exceptionCode.UserErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.error.excption.CustomUserException;
import com.cooknote.backend.global.infra.aws.s3.service.S3Service;
import com.cooknote.backend.global.utils.common.CommonFunctionUtil;
import com.cooknote.backend.mappers.RecipeMapper;
import com.cooknote.backend.mappers.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserMapper userMapper;
	private final RecipeMapper recipeMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final S3Service s3Service;	
	
	// 유저 조회
	@Override
	public User getLoginUser(String id, UserStatus userStatus) {

		return userMapper.getLoginUser(id, userStatus);
	}

	// 유저 프로필 정보 조회
	@Override
	public UserHostProfileResponseDTO getHostProfile(Long userId, Long hostId) {
		
		User host = userMapper.getUser(hostId);
		
		if(host == null) {
			throw new CustomCommonException(CommonErrorCode.NOT_FOUND_EXCEPTION);
		}
		
		if(host.getStatus() == UserStatus.SUSPEND) {
			throw new CustomUserException(UserErrorCode.SUSPEND_USER_EXCEPTION);
		}
		
		return userMapper.getHostProfile(userId, hostId, RecipeStatus.PUBLIC, RecipeStatus.PRIVATE, UserStatus.ACTIVE);
		
	}

	// 본인 프로필 정보 조회
	@Override
	public UserProfileResponseDTO getProfile(Long userId) {

		return userMapper.getProfile(userId, RecipeStatus.PUBLIC, RecipeStatus.PRIVATE, UserStatus.ACTIVE);
	}

	// 유저 팔로우 조회
	@Override
	public UserFollowResponseDTO getFollow(Long userId) {
		
		List<Follow> getFollower = userMapper.getFollower(userId, UserStatus.ACTIVE);
		
		List<Follow> getFollowing = userMapper.getFollowing(userId, UserStatus.ACTIVE);
		
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
		
		// 닉네임 변경 사항 체크
		if(!CommonFunctionUtil.match(newNickname, user.getNickname())) {
			updateNickname = newNickname;
		}
		
		// 이미지 변경 사항 체크
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

	// 유저 비밀번호 수정
	@Override
	@Transactional
	public void userPwUpdate(Long userId, @Valid UserPwEditRequestDTO userPwEditRequestDTO) {
		
		User user = userMapper.getUser(userId);
		
        // 기존 비밀번호가 일치하는지 확인
        if (!bCryptPasswordEncoder.matches(userPwEditRequestDTO.getCurrentPw(), user.getPassword())) {
            throw new CustomCommonException(CommonErrorCode.INPUT_MATCH_EXCEPTION);
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
		
		recipeMapper.recipeDeleteByUserId(userId, RecipeStatus.DELETE);
		recipeMapper.recipeSeqDeleteByUserId(userId);
		userMapper.userDelete(userId, UserStatus.DELETE);
		userMapper.userFollowAllDelete(userId);
		userMapper.userBookmarkAllDelete(userId);
		userMapper.userLikeAllDelete(userId);
		userMapper.userCommnetAllDelete(userId, CommentStatus.DELETE);
		
		// s3 삭제
		String recipeFolderPrefix = Constans.S3_MOVE_RECIPE_PATH + userId;
		String userFolderPrefix = Constans.S3_MOVE_USER_PROFILE_PATH + userId;
		s3Service.deleteFolderFromS3(recipeFolderPrefix);
		s3Service.deleteFolderFromS3(userFolderPrefix);
	}

	// 팔로잉 게시글 작성 최신순으로 가져오기 
	@Override
	public List<UserProfileEditInfoResponseDTO> getFollowingLatestForRecipe(Long userId) {
		
		return userMapper.getFollowingLatestForRecipe(userId, RecipeStatus.PUBLIC, UserStatus.ACTIVE);
	}

	// 신고 생성
	@Override
	public void reportInsert(Long userId, UserReportInsertRequestDTO userReportInsertRequestDTO) {
		
		userMapper.reportInsert(userId, userReportInsertRequestDTO, ReportType.RECIPE, ReportType.COMMENT);
	}

	// 신고 중복 확인
	@Override
	public void reportDuplicationCheck(long userId, UserReportDupliationCheckRequestDTO userReportDupliationCheckRequestDTO) {
		
		boolean reportDuplication = userMapper.reportDuplicationCheck(userId, userReportDupliationCheckRequestDTO, ReportType.RECIPE, ReportType.COMMENT);
		
		if(reportDuplication) {
			throw new CustomUserException(UserErrorCode.REPORT_DUPLICATION_EXCEPTION);
		}
	}

	// 신고 내역 가져오기
	@Override
	public Page<UserReportResponseDTO> getReport(Long userId, int page, int size) {
		
		int offset = page * size;
		List<UserReportResponseDTO> result = userMapper.getReports(userId, size, offset);
		int total = userMapper.getReportsCount(userId);
		
		List<UserReportResponseDTO> reportList = result.stream()
			    .peek(dto -> dto.setReportStatusLabel(dto.getReportStatus().getLabel()))  // 상태 라벨 설정
			    .toList();
		return new PageImpl<>(reportList, PageRequest.of(page, size), total);
	}
	
	// 제재 내역 가져오기
	@Override
	public Page<UserSacntionResponseDTO> getSanction(Long userId, int page, int size) {
		
		int offset = page * size;
		List<UserSacntionResponseDTO> result = userMapper.getSanction(userId, size, offset, ReportStatus.APPROVED);
		int total = userMapper.getSanctionCount(userId, ReportStatus.APPROVED);
		
		List<UserSacntionResponseDTO> sanctionList = result.stream()
			    .peek(dto -> dto.setReportLabel(dto.getReportType().getLabel()))  // 상태 라벨 설정
			    .toList();
		
		return new PageImpl<>(sanctionList, PageRequest.of(page, size), total);
	}
	
	
	// 쉐프 검색 - 게시글 0.2, 북마크 0.2, 좋아요 0.2, 팔로워 0.4 (인기순)
	@Override
	public Page<UserSearchChefResponseDTO> getSearchChefList(Long userId, String keyword, int page, int size) {
		
		int offset = page * size;
		List<UserSearchChefResponseDTO> chefList = userMapper.getSearchChefList(userId, keyword, size, offset, RecipeStatus.PUBLIC, UserStatus.ACTIVE);
		int total = userMapper.getSearchChefListCount(userId, keyword, RecipeStatus.PUBLIC, UserStatus.ACTIVE);
		
		return new PageImpl<>(chefList, PageRequest.of(page, size), total);
	}


}
