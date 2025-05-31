package com.cooknote.backend.domain.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cooknote.backend.domain.user.dto.request.UserProfileUpdateRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserPwEditRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserReportDupliationCheckRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserReportInsertRequestDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileEditInfoResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserFollowResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserHostProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserReportResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserSearchChefResponseDTO;
import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.domain.user.enums.UserStatus;

import jakarta.validation.Valid;

public interface UserService {

	User getLoginUser(String id, UserStatus delete);

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

	// 팔로잉 게시글 작성 최신순으로 가져오기
	List<UserProfileEditInfoResponseDTO> getFollowingLatestForRecipe(Long userId);

	// 신고 생성
	void reportInsert(Long userId, UserReportInsertRequestDTO userReportInsertRequestDTO);

	// 신고 중복 확인
	void reportDuplicationCheck(long userId, UserReportDupliationCheckRequestDTO userReportDupliationCheckRequestDTO);
	
	// 신고 내역 가져오기
	Page<UserReportResponseDTO> getReport(Long userId, int page, int size);

	// 쉐프 검색 - 게시글 0.2, 북마크 0.2, 좋아요 0.2, 팔로워 0.4 (인기순)
	Page<UserSearchChefResponseDTO> getSearchChefList(Long userId, String keyword, int page, int size);

	
}
