package com.example.assistant.domain.member.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class MemberResponse {
	private Long id;
	private String email;
	private String name;
	private String nickname;
	private LocalDateTime createdAt;

	public MemberResponse(Long id, String email, String name, String nickname, LocalDateTime createdAt) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.nickname = nickname;
		this.createdAt = createdAt;
	}
}
