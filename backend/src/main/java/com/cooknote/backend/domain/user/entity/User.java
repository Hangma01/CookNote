package com.cooknote.backend.domain.user.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private String userId;
	private String name;
	private String password;
	private String email;
	private String nickname;
	private String profileImage;
	private LocalDate createAt;
	private LocalDate updateAt;
	
	@Builder
	public User(String userId, String password, String name, String nickname, String email) {
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.email = email;
	}
	
	@Builder
	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}
}
