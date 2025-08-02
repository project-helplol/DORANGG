package com.example.assistant.domain.riot.Enum;

import lombok.Getter;

@Getter
public enum GameResult {
	WIN("승리"),  // 승리
	LOSE("패배");

	private final String description;

	GameResult(String description) {
		this.description = description;
	}
}


