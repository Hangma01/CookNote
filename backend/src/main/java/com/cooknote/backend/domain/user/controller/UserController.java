package com.cooknote.backend.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.user.dto.UserJoinRequestDTO;
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.error.CustomException;
import com.cooknote.backend.global.error.ErrorCode;
import com.cooknote.backend.global.infra.utils.message.ErrorMessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {


	private final UserService userService;
	

	// 아이디 중복 체크
	@GetMapping("/check-user-id")
	public ResponseEntity<String> getCheckUserId(@RequestParam("user_id") String userId) {
	    return checkDuplicate(userService.getCheckUserId(userId), ErrorMessageUtil.DUPLICATE_USER_ID);
	}

	// 닉네임 중복 체크
	@GetMapping("/check-nickname")
	public ResponseEntity<String> getCheckNickname(@RequestParam("nickname") String nickname) {
	    return checkDuplicate(userService.getCheckNickname(nickname), ErrorMessageUtil.DUPLICATE_NICKNAME);
	}

	// 이메일 중복 체크
	@GetMapping("/check-email")
	public ResponseEntity<String> getCheckEmail(@RequestParam("email") String email) {
	    return checkDuplicate(userService.getCheckEmail(email), ErrorMessageUtil.DUPLICATE_EMAIL);
	}
	
	// 회원 가입
	@PostMapping("/join")
	public ResponseEntity<String> userJoin(@RequestBody UserJoinRequestDTO userJoinRequestDTO, BindingResult bindingResult) {
		
		// 유효성 검사 확인
		if(bindingResult.hasErrors()) {
			throw new CustomException(ErrorCode.JOIN_VALIDATION_EXCEPTION);
		}
		
		userService.userJoin(userJoinRequestDTO);
		
	    return ResponseEntity.ok().build();
	}	
	
	// 중복 체크
	private ResponseEntity<String> checkDuplicate(boolean isExists, ErrorMessageUtil errorMessage) {
	    if (isExists) {
	        return ResponseEntity.ok(errorMessage.getMessage());
	    }
	    
	    return ResponseEntity.ok().build();
	}
}
