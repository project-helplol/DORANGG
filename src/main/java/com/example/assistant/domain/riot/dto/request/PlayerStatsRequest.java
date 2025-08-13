package com.example.assistant.domain.riot.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayerStatsRequest {
    private String gameName;
    private String tagLine;
}
