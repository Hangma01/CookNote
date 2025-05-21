package com.cooknote.backend.mappers;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cooknote.backend.domain.user.entity.User;


@Mapper
public interface UserMapper {
	
	// 회원 정보 가져오기
	User getUser(String id);
}
