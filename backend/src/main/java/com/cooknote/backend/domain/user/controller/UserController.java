package com.cooknote.backend.domain.user.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.auth.CustomUserDetails;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.utils.common.CommonFunctionUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	private final UserService userService;

	
	
	// 호스트 프로필 정보
	@GetMapping("/profile/host")
	public ResponseEntity<UserHostProfileResponseDTO> getHostPorfile(@AuthenticationPrincipal CustomUserDetails customUserDetails
			   							   						   , @RequestParam("hostId") Long hostId) {
		
		Long userId = null;
		
		if (CommonFunctionUtil.authCheck(customUserDetails)) {
			userId = customUserDetails.getUserId();
		}
		
		// Null 체크
		if(CommonFunctionUtil.nullCheck(hostId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		return ResponseEntity.ok(userService.getHostProfile(userId, hostId));
	}
	
	// 본인 프로필 정보
	@GetMapping("/profile")
	public ResponseEntity<UserProfileResponseDTO> getPorfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		
		return ResponseEntity.ok(userService.getProfile(customUserDetails.getUserId()));
	}
	
	// 본인 팔로우 정보
	@GetMapping("/follow")
	public ResponseEntity<UserFollowResponseDTO> getFollow(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		
		return ResponseEntity.ok(userService.getFollow(customUserDetails.getUserId()));
	}
	
	// 팔로우 생성
	@PostMapping("/follow")
	public ResponseEntity<Void> insetFollow(@AuthenticationPrincipal CustomUserDetails customUserDetails
														   , @RequestBody Long followId) {
		

		// Null 체크
		if(CommonFunctionUtil.nullCheck(followId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		Long userId = customUserDetails.getUserId();
		if(userId == followId) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		userService.insertFollow(customUserDetails.getUserId(), followId);
		
		return ResponseEntity.ok().build();
	}
	
	// 팔로우 취소
	@DeleteMapping("/follow")
	public ResponseEntity<Void> deleteFollow(@AuthenticationPrincipal CustomUserDetails customUserDetails
															, @RequestParam("followId") Long followId) {
		
		// Null 체크
		if(CommonFunctionUtil.nullCheck(followId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		userService.deleteFollow(customUserDetails.getUserId(), followId);
		return ResponseEntity.ok().build();
	}
	
	// 회원정보 수정 데이터 가져오기
	@GetMapping("/edit")
	public ResponseEntity<UserProfileEditInfoResponseDTO> getUserProfileEditInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
		
		return ResponseEntity.ok(userService.getUserProfileEditInfo(customUserDetails.getUserId()));
	}
	
	// 회원정보 수정
	@PatchMapping("/edit")
	public ResponseEntity<Void> userProfileUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails
											 	, @Valid @RequestBody UserProfileUpdateRequestDTO userProfileUpdateRequestDTO
											 	, BindingResult bindingResult) {

		// 유효성 검사 확인
		if (CommonFunctionUtil.validationCheck(bindingResult)) {
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		userService.userProfileUpdate(customUserDetails.getUserId(), userProfileUpdateRequestDTO);
		return ResponseEntity.ok().build();
	}
	
	
	// 비밀번호 수정
	@PatchMapping("/edit/pw")
	public ResponseEntity<Void> userPwUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails
										 , @Valid @RequestBody UserPwEditRequestDTO userPwEditRequestDTO
										 , BindingResult bindingResult) {

		// 유효성 검사 확인
		if (CommonFunctionUtil.validationCheck(bindingResult)) {;
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		userService.userPwUpdate(customUserDetails.getUserId(), userPwEditRequestDTO);
		
		return ResponseEntity.ok().build();
	}
	
	// 회원 탈퇴
	@DeleteMapping("")
	public ResponseEntity<Void> userDelete(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		
		userService.userDelete(customUserDetails.getUserId());
		
		return ResponseEntity.ok().build();
	}

	
	// 팔로잉 게시글 작성 최신순으로 가져오기 
	@GetMapping("/following/latest")
	public ResponseEntity<List<UserProfileEditInfoResponseDTO>> getFollowingLatestForRecipe(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		
		
		return ResponseEntity.ok(userService.getFollowingLatestForRecipe(customUserDetails.getUserId()));
	}
	
	// 신고 생성
	@PostMapping("/report")
	public ResponseEntity<List<UserProfileEditInfoResponseDTO>> reportInsert(@AuthenticationPrincipal CustomUserDetails customUserDetails
																		   , @Valid @RequestBody UserReportInsertRequestDTO userReportInsertRequestDTO
																		   , BindingResult bindingResult) {
		// 유효성 검사 확인
		if (CommonFunctionUtil.validationCheck(bindingResult)) {
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		if (CommonFunctionUtil.nullCheck(userReportInsertRequestDTO.getRecipeId()) && CommonFunctionUtil.nullCheck(userReportInsertRequestDTO.getCommentId())) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		userService.reportInsert(customUserDetails.getUserId(), userReportInsertRequestDTO);

		return ResponseEntity.ok().build();
	}
	
	// 신고 중복 체크
	@PostMapping("/report/duplication")
	public ResponseEntity<List<UserProfileEditInfoResponseDTO>> reportDuplicationCheck(@AuthenticationPrincipal CustomUserDetails customUserDetails
																				     , @Valid @RequestBody UserReportDupliationCheckRequestDTO userReportDupliationCheckRequestDTO
																				     , BindingResult bindingResult) {
		// 유효성 검사 확인
		if (CommonFunctionUtil.validationCheck(bindingResult)) {
		throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		if (CommonFunctionUtil.nullCheck(userReportDupliationCheckRequestDTO.getRecipeId()) && CommonFunctionUtil.nullCheck(userReportDupliationCheckRequestDTO.getCommentId())) {
		throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		
		userService.reportDuplicationCheck(customUserDetails.getUserId(), userReportDupliationCheckRequestDTO);
		
		return ResponseEntity.ok().build();
	}
	
	// 신고 내역 가져오기
	@GetMapping("/report")
	public ResponseEntity<Page<UserReportResponseDTO>> getReport (@AuthenticationPrincipal CustomUserDetails customUserDetails 
																, @RequestParam(value = "page", defaultValue = "0") int page
																, @RequestParam(value = "size", defaultValue = "10") int size) {

	return ResponseEntity.ok(userService.getReport(customUserDetails.getUserId(), page, size));
	
	}
	
	// 제재 내역 가져오기
	@GetMapping("/report/sanction")
	public ResponseEntity<Page<UserSacntionResponseDTO>> getSanction (@AuthenticationPrincipal CustomUserDetails customUserDetails 
															   	  , @RequestParam(value = "page", defaultValue = "0") int page
															   	  , @RequestParam(value = "size", defaultValue = "10") int size) {
	
		return ResponseEntity.ok(userService.getSanction(customUserDetails.getUserId(), page, size));
	
	}
	
	// 쉐프 검색 - 게시글 0.2, 북마크 0.2, 좋아요 0.2, 팔로워 0.4 (인기순)
	@GetMapping("/search/chef")
	public ResponseEntity<Page<UserSearchChefResponseDTO>> getSearchChefList(@AuthenticationPrincipal CustomUserDetails customUserDetails
																		   , @RequestParam(value = "keyword", required = false) String keyword
																		   , @RequestParam(value = "page", defaultValue = "0") int page
																		   , @RequestParam(value = "size", defaultValue = "30") int size){

		Long userId = null;
		
		if (CommonFunctionUtil.authCheck(customUserDetails)) {
			userId = customUserDetails.getUserId();
		}
		
		return ResponseEntity.ok(userService.getSearchChefList(userId, keyword, page, size));
	}
	
}
