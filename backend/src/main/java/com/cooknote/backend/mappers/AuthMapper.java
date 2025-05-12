package com.cooknote.backend.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.user.entity.User;

@Mapper
public interface AuthMapper {

	
	// 아이디 중복 체크
	boolean getCheckUserId(@Param("userId") String userId);
	
	// 닉네임 중복 체크
	boolean getCheckNickname(@Param("nickname") String nickname);

	// 이메일 중복 체크
	boolean getCheckEmail(@Param("email") String email);

	// 회원가입
	void userJoin(User user);

	// 아이디 찾기 - 요청
	boolean userFindIdAuthRequest(User user);

	// 아이디 찾기
	User userFindId(User user);

	// 비밀번호 찾기
	User userFindPw(User User);

	void updatePwReset(User User);
}
