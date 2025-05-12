package com.cooknote.backend.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.user.dto.request.UserFindIdAuthRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserFindPwAuthRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserFindPwResetRequestDTO;
import com.cooknote.backend.domain.user.dto.request.UserJoinRequestDTO;
import com.cooknote.backend.domain.user.dto.response.UserFindIdResponseDTO;
import com.cooknote.backend.domain.user.dto.response.UserFindPwResponseDTO;
import com.cooknote.backend.domain.user.service.UserService;
import com.cooknote.backend.global.error.CustomException;
import com.cooknote.backend.global.error.ErrorCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	// 아이디 중복 체크
	@GetMapping("/check-user-id")
	public ResponseEntity<Void> getCheckUserId(@RequestParam("user_id") String userId) {

		checkDuplicate(userService.getCheckUserId(userId), ErrorCode.DUPLICATE_USERID_EXCEPTION);

		return ResponseEntity.ok().build();
	}

	// 닉네임 중복 체크
	@GetMapping("/check-nickname")
	public ResponseEntity<Void> getCheckNickname(@RequestParam("nickname") String nickname) {

		checkDuplicate(userService.getCheckNickname(nickname), ErrorCode.DUPLICATE_NICKNAME_EXCEPTION);

		return ResponseEntity.ok().build();
	}

	// 이메일 중복 체크
	@GetMapping("/check-email")
	public ResponseEntity<Void> getCheckEmail(@RequestParam("email") String email) {

		checkDuplicate(userService.getCheckEmail(email), ErrorCode.DUPLICATE_EMAIL_EXCEPTION);

		return ResponseEntity.ok().build();
	}

	// 회원 가입
	@PostMapping("/join")
	public ResponseEntity<Void> userJoin(@Valid @RequestBody UserJoinRequestDTO userJoinRequestDTO, BindingResult bindingResult) {

		// 유효성 검사 확인
		if (bindingResult.hasErrors()) {
			throw new CustomException(ErrorCode.VALIDATION_EXCEPTION);
		}

		// 중복 검사
		checkDuplicate(userService.getCheckUserId(userJoinRequestDTO.getUserId()), ErrorCode.DUPLICATE_USERID_EXCEPTION);
		checkDuplicate(userService.getCheckNickname(userJoinRequestDTO.getNickname()), ErrorCode.DUPLICATE_NICKNAME_EXCEPTION);
		checkDuplicate(userService.getCheckEmail(userJoinRequestDTO.getEmail()), ErrorCode.DUPLICATE_EMAIL_EXCEPTION);

		// 회원 가입
		userService.userJoin(userJoinRequestDTO);

		return ResponseEntity.ok().build();
	}

	// 아이디 찾기 - 요청
	@PostMapping("/find-id/auth")
	public ResponseEntity<Void> userFindIdAuthRequest(@RequestBody UserFindIdAuthRequestDTO userFindIdAuthRequestDTO) {

		userService.userFindIdAuthRequest(userFindIdAuthRequestDTO);

		return ResponseEntity.ok().build();
	}

	// 아이디 찾기 - 아이디 반환
	@GetMapping("/find-id")
	public ResponseEntity<UserFindIdResponseDTO> userFindId(@RequestParam("name") String name, @RequestParam("email") String email) {

		return ResponseEntity.ok(userService.userFindId(name, email));
	}

	// 비밀번호 찾기 - 요청
	@PostMapping("/find-pw/auth")
	public ResponseEntity<UserFindPwResponseDTO> userFindPwAuthRequest(@RequestBody UserFindPwAuthRequestDTO userFindPwAuthRequestDTO) {
		
		return ResponseEntity.ok(userService.userFindPwAuthRequest(userFindPwAuthRequestDTO));
	}
	
	
	// 비밀번호 찾기 - 변경
	@PostMapping("/find-pw/reset")
	public ResponseEntity<Void> userFindPwReset(@Valid @RequestBody UserFindPwResetRequestDTO userFindPwResetRequestDTO, BindingResult bindingResult) {

		// 유효성 검사 확인
		if (bindingResult.hasErrors()) {
			throw new CustomException(ErrorCode.VALIDATION_EXCEPTION);
		}
		
		userService.userFindPwReset(userFindPwResetRequestDTO);
		
		return ResponseEntity.ok().build();
	}

	// 중복 체크
	private void checkDuplicate(boolean isDuplicate, ErrorCode errorCode) {
		if (isDuplicate) {
			throw new CustomException(errorCode);
		}
	}
}
