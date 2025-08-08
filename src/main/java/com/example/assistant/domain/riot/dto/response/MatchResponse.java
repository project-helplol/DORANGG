package com.example.assistant.domain.riot.dto.response;

import com.example.assistant.domain.riot.enums.GameResult;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MatchResponse {
    private String matchId;
    private GameResult result;
    private String kda;
    private String championName;
    private LocalDateTime matchDateTime; // erd 수정요망
    private String teamPosition;
    private int gameDuration;
}
