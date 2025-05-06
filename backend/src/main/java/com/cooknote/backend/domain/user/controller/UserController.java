package com.cooknote.backend.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.user.dto.UserDTO;
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.infra.utils.message.ErrorMessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {


	private final UserService userService;
	

	// 아이디 중복 체크
	@GetMapping("/check-login-id")
	public ResponseEntity<String> getCheckLoginId(@RequestParam("login_id") String loginId) {
	    return checkDuplicate(userService.getCheckLoginId(loginId), ErrorMessageUtil.DUPLICATE_LOGIN_ID);
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
	public ResponseEntity<String> userJoin(@RequestBody UserDTO userDTO) {
		userService.userJoin(userDTO);
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
