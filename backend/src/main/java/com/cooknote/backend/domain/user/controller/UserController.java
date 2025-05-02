package com.cooknote.backend.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.error.ErrorMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {


	private final UserService userService;
	

	// 아이디 중복 체크
	@GetMapping("/check-login-id")
	public ResponseEntity<String> getCheckLoginId(@RequestParam("login_id") String loginId) {

		boolean isExists = userService.getCheckLoginId(loginId);
		
		if(isExists) {
			// 중복 발생 예외처리
			return ResponseEntity.ok(ErrorMessage.DUPLICATE_LOGIN_ID.getMessage());	
		}
		
		return ResponseEntity.ok().build();
	}
	
	// 닉네임 중복 체크
	@GetMapping("/check-nickname")
	public ResponseEntity<String> getCheckNickname(@RequestParam("nickname") String nickname) {

		boolean isExists = userService.getCheckNickname(nickname);
		
		if(isExists) {
			// 중복 발생 예외처리
			return ResponseEntity.ok(ErrorMessage.DUPLICATE_NICKNAME.getMessage());	
		}
		
		return ResponseEntity.ok().build();
	}
	
	// 이메일 중복 체크
	@GetMapping("/check-email")
	public ResponseEntity<String> getCheckEmail(@RequestParam("email") String email) {

		boolean isExists = userService.getCheckEmail(email);
		
		if(isExists) {
			// 중복 발생 예외처리
			return ResponseEntity.ok(ErrorMessage.DUPLICATE_EMAIL.getMessage());	
		}
		
		return ResponseEntity.ok().build();
	}
}
