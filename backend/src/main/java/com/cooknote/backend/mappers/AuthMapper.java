package com.cooknote.backend.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.auth.dto.request.UserFindIdAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserFindPwAuthRequestDTO;
import com.cooknote.backend.domain.auth.dto.request.UserJoinRequestDTO;
import com.cooknote.backend.domain.user.entity.User;
import com.cooknote.backend.domain.user.enums.UserStatus;

@Mapper
public interface AuthMapper {

	
	// 아이디 중복 체크
	boolean getExistsId(@Param("id") String id
					  , @Param("statusUserDelete") UserStatus statusUserDelete);
	
	// 닉네임 중복 체크
	boolean getExistsNickname(@Param("nickname") String nickname
							, @Param("statusUserDelete") UserStatus statusUserDelete);

	// 이메일 중복 체크
	boolean getExistsEmail(@Param("email") String email
						 , @Param("statusUserDelete") UserStatus statusUserDelete);

	// 회원가입
	void userJoin(User user);

	// 아이디 찾기 - 요청
	boolean userFindIdAuthRequest(@Param("name") String name
								, @Param("email") String email
								, @Param("statusUserDelete") UserStatus statusUserDelete);

	// 아이디 찾기
	User userFindId(@Param("name") String name
				  , @Param("email") String email
				  , @Param("statusUserDelete") UserStatus statusUserDelete);

	// 비밀번호 찾기
	User userFindPw(@Param("id") String id
				  , @Param("email") String email
				  , @Param("statusUserDelete") UserStatus statusUserDelete);

	void updatePwReset(@Param("userId") Long userId
					 , @Param("password") String password
					 , @Param("statusUserDelete") UserStatus statusUserDelete);
}
