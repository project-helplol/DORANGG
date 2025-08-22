package com.example.assistant.domain.riot.dto.response;

import com.example.assistant.domain.riot.enums.GameResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@Builder
public class MatchResponse {
    private String matchId;
    private String result;
    private String kda;
    private String championName;
    private LocalDateTime matchDateTime; // erd 수정요망
    private String teamPosition;
    private int gameDuration;
    private String gameType;
    private int queueId;
    private Integer profileIconId;
    private Long summonerLevel;
}
