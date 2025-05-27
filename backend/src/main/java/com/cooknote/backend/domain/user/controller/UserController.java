package com.cooknote.backend.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.auth.dto.request.UserFindPwResetRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserProfileUpdateRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserPwEditRequestDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileEditInfoResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserFollowResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserHostProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileResponseDTO;
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
			   							   , @RequestParam(value = "hostId") Long hostId) {
		
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
	
	// 프로필 수정 정보
	@GetMapping("/edit")
	public ResponseEntity<UserProfileEditInfoResponseDTO> getUserProfileEditInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
		return ResponseEntity.ok(userService.getUserProfileEditInfo(customUserDetails.getUserId()));
	}
	
	// 프로필 업데이트
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

	
}
