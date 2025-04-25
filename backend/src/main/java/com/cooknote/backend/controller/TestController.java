package com.cooknote.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooknote.backend.domain.user.UserDTO;
import com.cooknote.backend.mappers.UserMapper;

@RestController
public class TestController {
	
	private UserMapper userMapper;
	
   public TestController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
	
	@GetMapping("/")
	public ResponseEntity<?> test(){
		
		List<UserDTO> userDTO = userMapper.find();
		for(UserDTO user : userDTO) {
			System.out.println(user.getUserId());
		}
		
		return ResponseEntity.badRequest().build();
	}
}
