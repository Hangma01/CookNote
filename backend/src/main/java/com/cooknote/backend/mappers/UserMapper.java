package com.cooknote.backend.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cooknote.backend.domain.user.UserDTO;

@Mapper
public interface UserMapper {
	// 유저 찾기
	List<UserDTO> find();

}
