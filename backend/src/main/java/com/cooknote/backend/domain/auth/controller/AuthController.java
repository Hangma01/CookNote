package com.cooknote.backend.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.cooknote.backend.global.error.exceptionCode.CommonErrorCode;
import com.cooknote.backend.global.error.excption.CustomCommonException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

	private final AuthService authService;
	
	// 아이디 중복 체크
	@GetMapping("/existsId")
	public ResponseEntity<Void> getExistsId(@RequestParam("id") String id) {

		authService.getExistsId(id);

		return ResponseEntity.ok().build();
	}

	// 닉네임 중복 체크
	@GetMapping("/existsNickname")
	public ResponseEntity<Void> getExistsNickname(@RequestParam("nickname") String nickname) {

		authService.getExistsNickname(nickname);

		return ResponseEntity.ok().build();
	}

	// 이메일 중복 체크
	@GetMapping("/existsEmail")
	public ResponseEntity<Void> getExistsEmail(@RequestParam("email") String email) {

		authService.getExistsEmail(email);

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
		authService.getExistsId(userJoinRequestDTO.getId());
		authService.getExistsNickname(userJoinRequestDTO.getNickname());
		authService.getExistsEmail(userJoinRequestDTO.getEmail());

		// 회원 가입
		authService.userJoin(userJoinRequestDTO);

		return ResponseEntity.ok().build();
	}

	// 아이디 찾기 - 요청
	@PostMapping("/findId")
	public ResponseEntity<Void> userFindIdAuthRequest(@RequestBody UserFindIdAuthRequestDTO userFindIdAuthRequestDTO) {
		
		authService.userFindIdAuthRequest(userFindIdAuthRequestDTO);

		return ResponseEntity.ok().build();
	}

	// 아이디 찾기 - 아이디 반환
	@GetMapping("/findId")
	public ResponseEntity<UserFindIdResponseDTO> userFindId(@RequestParam("name") String name, @RequestParam("email") String email) {

		return ResponseEntity.ok(authService.userFindId(name, email));
	}

	// 비밀번호 찾기 - 요청
	@PostMapping("/findPw")
	public ResponseEntity<UserFindPwResponseDTO> userFindPwAuthRequest(@RequestBody UserFindPwAuthRequestDTO userFindPwAuthRequestDTO) {
		
		return ResponseEntity.ok(authService.userFindPwAuthRequest(userFindPwAuthRequestDTO));
	}
	
	
	// 비밀번호 찾기 - 변경
	@PatchMapping("/findPw")
	public ResponseEntity<Void> userFindPwReset(@Valid @RequestBody UserFindPwResetRequestDTO userFindPwResetRequestDTO, BindingResult bindingResult) {

		// 유효성 검사 확인
		if (bindingResult.hasErrors()) {
			throw new CustomCommonException(CommonErrorCode.VALIDATION_EXCEPTION);
		}
		
		authService.userFindPwReset(userFindPwResetRequestDTO);
		
		return ResponseEntity.ok().build();
	}
		
	// 토큰 재발급
	@PostMapping("/reissue")
	 public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
		authService.reissue(request, response);

		return ResponseEntity.ok().build();
    }
}
