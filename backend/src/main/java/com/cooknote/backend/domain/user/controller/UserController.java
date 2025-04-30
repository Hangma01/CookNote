package com.cooknote.backend.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.error.CustomException;
import com.cooknote.backend.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {


	private final UserService userService;
	

	// 아이디 중복 체크
	@GetMapping("/exists/{loginId}")
	public ResponseEntity<Void> getExistsLoginId(@PathVariable("loginId") String loginId) {

		boolean isExists = userService.getExistsLoginId(loginId);
		
		if(isExists) {
			// 중복 발생 예외처리
			throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);	
		}
		
		return ResponseEntity.ok().build();
	}
}
