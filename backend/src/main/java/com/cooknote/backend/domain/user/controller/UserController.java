package com.cooknote.backend.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.user.dto.response.UserFollowResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserHostProfileResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.auth.CustomUserDetails;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;
import com.cooknote.backend.global.utils.common.CommonFunctionUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
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
	
	@PostMapping("/follow")
	public ResponseEntity<Void> insetFollow(@AuthenticationPrincipal CustomUserDetails customUserDetails
														   , @RequestBody Long followId) {
		
		// Null 체크
		if(CommonFunctionUtil.nullCheck(followId)) {
			throw new CustomCommonException(CommonErrorCode.INVALID_STATE_EXCEPTION);
		}
		userService.insertFollow(customUserDetails.getUserId(), followId);
		
		return ResponseEntity.ok().build();
	}
	
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
}
