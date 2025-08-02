package com.example.assistant.domain.member.dto;

import lombok.Getter;

@Getter
public class SignupRequest {
	private String email;
	private String password;
	private String name;
	private String nickname;
}
