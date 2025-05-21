package com.cooknote.backend.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.auth.dto.request.UserFindIdAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserFindPwAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserJoinRequestDTO;
import com.cooknote.backend.domain.user.entity.User;

@Mapper
public interface AuthMapper {

	
	// 아이디 중복 체크
	boolean getExistsId(String id);
	
	// 닉네임 중복 체크
	boolean getExistsNickname(String nickname);

	// 이메일 중복 체크
	boolean getExistsEmail(String email);

	// 회원가입
	void userJoin(User user);

	// 아이디 찾기 - 요청
	boolean userFindIdAuthRequest(@Param("name") String name,@Param("email") String email);

	// 아이디 찾기
	User userFindId(@Param("name") String name, @Param("email") String email);

	// 비밀번호 찾기
	User userFindPw(@Param("id") String id, @Param("email") String email);

	void updatePwReset(@Param("userId") long userId, @Param("password") String password);
}
