package com.example.assistant.domain.riot.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlayerStatsResponse {
    private String tier;
    private String topChampion;
    private List<String> top3Champions;
    private double winRate;
    private String favoritePosition;
    private String mostPlayedChampion;
    private Integer profileIconId;
    private Long summonerLevel;
}
