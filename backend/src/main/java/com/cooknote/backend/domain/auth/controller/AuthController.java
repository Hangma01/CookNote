package com.cooknote.backend.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.auth.dto.request.UserFindIdAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserFindPwAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserFindPwResetRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserJoinRequestDTO;
import com.cooknote.backend.domain.auth.dto.response.UserFindIdResponseDTO;
import com.cooknote.backend.domain.auth.dto.response.UserFindPwResponseDTO;
import com.cooknote.backend.domain.auth.service.AuthService;
import com.cooknote.backend.global.error.exceptionCode.AuthErrorCode;
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.excption.CustomAuthException;
import com.cooknote.backend.global.error.excption.CustomCommonException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	
	// 아이디 중복 체크
	@GetMapping("/check-user-id")
	public ResponseEntity<Void> getCheckUserId(@RequestParam("user_id") String userId) {

		checkDuplicate(authService.getCheckUserId(userId), AuthErrorCode.DUPLICATE_USERID_EXCEPTION);

		return ResponseEntity.ok().build();
	}

	// 닉네임 중복 체크
	@GetMapping("/check-nickname")
	public ResponseEntity<Void> getCheckNickname(@RequestParam("nickname") String nickname) {

		checkDuplicate(authService.getCheckNickname(nickname), AuthErrorCode.DUPLICATE_NICKNAME_EXCEPTION);

		return ResponseEntity.ok().build();
	}

	// 이메일 중복 체크
	@GetMapping("/check-email")
	public ResponseEntity<Void> getCheckEmail(@RequestParam("email") String email) {

		checkDuplicate(authService.getCheckEmail(email), AuthErrorCode.DUPLICATE_EMAIL_EXCEPTION);

		return ResponseEntity.ok().build();
	}

	// 회원 가입
	@PostMapping("/join")
	public ResponseEntity<Void> userJoin(@Valid @RequestBody UserJoinRequestDTO userJoinRequestDTO, BindingResult bindingResult) {

		// 유효성 검사 확인
		if (bindingResult.hasErrors()) {
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}

		// 중복 검사
		checkDuplicate(authService.getCheckUserId(userJoinRequestDTO.getUserId()), AuthErrorCode.DUPLICATE_USERID_EXCEPTION);
		checkDuplicate(authService.getCheckNickname(userJoinRequestDTO.getNickname()), AuthErrorCode.DUPLICATE_NICKNAME_EXCEPTION);
		checkDuplicate(authService.getCheckEmail(userJoinRequestDTO.getEmail()), AuthErrorCode.DUPLICATE_EMAIL_EXCEPTION);

		// 회원 가입
		authService.userJoin(userJoinRequestDTO);

		return ResponseEntity.ok().build();
	}

	// 아이디 찾기 - 요청
	@PostMapping("/find-id")
	public ResponseEntity<Void> userFindIdAuthRequest(@RequestBody UserFindIdAuthRequestDTO userFindIdAuthRequestDTO) {

		authService.userFindIdAuthRequest(userFindIdAuthRequestDTO);

		return ResponseEntity.ok().build();
	}

	// 아이디 찾기 - 아이디 반환
	@GetMapping("/find-id")
	public ResponseEntity<UserFindIdResponseDTO> userFindId(@RequestParam("name") String name, @RequestParam("email") String email) {

		return ResponseEntity.ok(authService.userFindId(name, email));
	}

	// 비밀번호 찾기 - 요청
	@PostMapping("/find-pw")
	public ResponseEntity<UserFindPwResponseDTO> userFindPwAuthRequest(@RequestBody UserFindPwAuthRequestDTO userFindPwAuthRequestDTO) {
		
		return ResponseEntity.ok(authService.userFindPwAuthRequest(userFindPwAuthRequestDTO));
	}
	
	
	// 비밀번호 찾기 - 변경
	@PostMapping("/find-pw/reset")
	public ResponseEntity<Void> userFindPwReset(@Valid @RequestBody UserFindPwResetRequestDTO userFindPwResetRequestDTO, BindingResult bindingResult) {

		// 유효성 검사 확인
		if (bindingResult.hasErrors()) {
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		authService.userFindPwReset(userFindPwResetRequestDTO);
		
		return ResponseEntity.ok().build();
	}
	
	
	// 중복 체크
	private void checkDuplicate(boolean isDuplicate, AuthErrorCode errorCode) {
		if (isDuplicate) {
			throw new CustomAuthException(errorCode);
		}
	}
	
	
	// 토큰 재발급
	@PostMapping("/reissue")
	 public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

		authService.reissue(request, response);

		return ResponseEntity.ok().build();
    }
}
