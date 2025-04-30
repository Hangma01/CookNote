package com.cooknote.backend.mappers;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMapper {
	
	// 아이디 중복 체크
	boolean getExistsLoginId(@Param("loginId") String loginId);

}
