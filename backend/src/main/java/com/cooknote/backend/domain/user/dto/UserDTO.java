package com.cooknote.backend.domain.user.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private int userId;
    private String loginId;
    private String name;
    private String password;
    private String email;
    private String nickname;
    private String profileImage;
    private Date createAt;
    private Date updateAt;
}
