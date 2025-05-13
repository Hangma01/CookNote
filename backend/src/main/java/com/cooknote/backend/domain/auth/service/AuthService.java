package com.cooknote.backend.domain.auth.service;

import org.springframework.stereotype.Service;

import com.cooknote.backend.domain.auth.dto.request.UserFindIdAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserFindPwAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserFindPwResetRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserJoinRequestDTO;
import com.cooknote.backend.domain.auth.dto.response.UserFindIdResponseDTO;
import com.cooknote.backend.domain.auth.dto.response.UserFindPwResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public interface AuthService {

	void getExistsId(String id);
	void getExistsNickname(String nickname);
	void getExistsEmail(String email);
	void userJoin(UserJoinRequestDTO userJoinRequestDTO);
	void userFindIdAuthRequest(UserFindIdAuthRequestDTO userFindIdAuthRequestDTO);
	UserFindIdResponseDTO userFindId(String name, String email);
	UserFindPwResponseDTO userFindPwAuthRequest(UserFindPwAuthRequestDTO userFindPwAuthRequestDTO);
	void userFindPwReset(UserFindPwResetRequestDTO userFindPwResetRequestDTO);
	void reissue(HttpServletRequest request, HttpServletResponse response);
	
}
