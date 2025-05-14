package com.cooknote.backend.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	
	@GetMapping("/mypage")
	public ResponseEntity<String> MyPage() {
		System.out.println("mypage 탐");
		
		return ResponseEntity.ok("성공");
	}

}
