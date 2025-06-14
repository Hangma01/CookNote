package com.cooknote.backend.mappers;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.user.dto.response.UserProfileEditInfoResponseDTO;
import com.cooknote.backend.domain.comment.enums.CommentStatus;
import com.cooknote.backend.domain.recipe.enums.RecipeStatus;
import com.cooknote.backend.domain.user.dto.request.UserReportDupliationCheckRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserReportInsertRequestDTO;
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


@Mapper
public interface UserMapper {
	
	// 로그인 회원 정보 가져오기
	User getLoginUser(@Param("id") String id
			        , @Param("statusUserDelete") UserStatus statusUserDelete);
	
	// 회원 정보 가져오기
	User getUser(@Param("userId") Long userId);

	// 유저 호스트 프로필 정보 가져오기	
	UserHostProfileResponseDTO getHostProfile(@Param("userId") Long userId
											, @Param("hostId") Long hostId
											, @Param("statusRecipePublic") RecipeStatus statusRecipePublic
											, @Param("statusRecipePrivate") RecipeStatus statusRecipePrivate
											, @Param("statusUserActive") UserStatus statusUserActive);

	// 유저 본인 프로필 정보 가져오기	
	UserProfileResponseDTO getProfile(@Param("userId") Long userId
									, @Param("statusRecipePublic") RecipeStatus statusRecipePublic
									, @Param("statusRecipePrivate") RecipeStatus statusRecipePrivate
									, @Param("statusUserActive") UserStatus statusUserActive);

	// 팔로워한 유저 정보 가져오기
	List<Follow> getFollower(@Param("userId") Long userId
						   , @Param("statusUserActive") UserStatus statusUserActive);

	// 팔로잉한 유저 정보 가져오기	
	List<Follow> getFollowing(@Param("userId") Long userId
						    , @Param("statusUserActive") UserStatus statusUserActive);

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
	void userDelete(@Param("userId") Long userId
				  , @Param("statusUserDelete") UserStatus statusUserDelete);

	// 팔로잉한 유저중 레시피 작성 최신순으로 정보 가져오기
	List<UserProfileEditInfoResponseDTO> getFollowingLatestForRecipe(@Param("userId") Long userId
							   	   								   , @Param("statusRecipePublic") RecipeStatus statusRecipePublic
							   	   								   , @Param("statusUserActive") UserStatus statusUserActive);

	// 신고 생성
	void reportInsert(@Param("userId") Long userId
			        , @Param("reportInsertReq") UserReportInsertRequestDTO userReportInsertRequestDTO
			        , @Param("recipe") ReportType recipe
			        , @Param("comment") ReportType comment);
	
	// 중복 신고 확인
	boolean reportDuplicationCheck(@Param("userId") Long userId
								 , @Param("reportDuplicationCheckReq") UserReportDupliationCheckRequestDTO userReportDupliationCheckRequestDTO
							     , @Param("recipe") ReportType recipe
							     , @Param("comment") ReportType comment);

	// 회원 탈퇴 시 팔로우 삭제
	void userFollowAllDelete(@Param("userId") Long userId);

	// 회원 탈퇴 시 북마크 삭제
	void userBookmarkAllDelete(@Param("userId") Long userId);

	// 회원 탈퇴 시 좋아요 삭제
	void userLikeAllDelete(@Param("userId") Long userId);

	// 회원 탈퇴 시 댓글 삭제
	void userCommnetAllDelete(@Param("userId") Long userId
							, @Param("statusCommentDelete") CommentStatus statusCommentDelete);

	// 쉐프 검색 - 게시글 0.2, 북마크 0.3, 팔로워 0.5 (인기순)
	List<UserSearchChefResponseDTO> getSearchChefList(@Param("userId") Long userId
													, @Param("keyword") String keyword
													, @Param("size") int size
													, @Param("offset") int offset
													, @Param("statusRecipePublic") RecipeStatus statusRecipePublic
													, @Param("statusUserActive") UserStatus statusUserActive 
													, @Param("chefFollowWeight") double chefFollowWeight
													, @Param("chefRecipeWeight") double chefRecipeWeight
													, @Param("chefLikeWeight") double chefLikeWeight
													, @Param("chefBookmarkWeight") double chefBookmarkWeight);

	// 신고 내역 가져오기
	List<UserReportResponseDTO> getReports(@Param("userId") Long userId
										 , @Param("size") int size
										 , @Param("offset") int offset
										 , @Param("reportTypeComment") ReportType reportTypeComment
										 , @Param("reportTypeCommentText") String reportTypeCommentText
										 , @Param("reportTypeReplyText") String reportTypeReplyText
										 , @Param("reportTypeRecipeText") String reportTypeRecipeText);
	
	// 신고 내역 카운트 가져오기
	int getReportsCount(@Param("userId") Long userId);
	
	// 제재 내역 가져오기
	List<UserSacntionResponseDTO> getSanction(@Param("userId") Long userId
											, @Param("size") int size
											, @Param("offset") int offset
											, @Param("statusReportApproved") ReportStatus statusReportApproved
											, @Param("reportTypeRecipe") ReportType reportTypeRecipe
											, @Param("reportTypeComment") ReportType reportTypeComment
											, @Param("reportTypeCommentText") String reportTypeCommentText
											, @Param("reportTypeReplyText") String reportTypeReplyText
											, @Param("reportTypeRecipeText") String reportTypeRecipeText);
	
	// 제재 내역 카운트
	int getSanctionCount(@Param("userId") Long userId
					   , @Param("statusReportApproved") ReportStatus statusReportApproved);
	
	
	// 쉐프 검색된 토탈 갯수
	int getSearchChefListCount(@Param("userId") Long userId
							 , @Param("keyword") String keyword
							 , @Param("statusRecipePublic") RecipeStatus statusRecipePublic
							 , @Param("statusUserActive") UserStatus statusUserActive);





}
