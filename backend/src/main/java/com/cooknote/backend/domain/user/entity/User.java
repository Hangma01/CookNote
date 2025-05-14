package com.cooknote.backend.domain.user.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	private long userId;
	private String id;
	private String name;
	private String password;
	private String email;
	private String nickname;
	private String profileImage;
	private LocalDate createAt;
	private LocalDate updateAt;
	private boolean isDelete;
}
