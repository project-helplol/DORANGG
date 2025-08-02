package com.example.assistant.domain.riot.dto.response;

import com.example.assistant.domain.riot.Enum.GameResult;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MatchResponse {
    private String matchId;
    private String championName;
    private String kda;
    private GameResult result;
    private LocalDateTime playDate;
    private Integer duration;
}
