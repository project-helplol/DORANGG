package com.example.assistant.domain.riot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyBriefingRequest {
    private String gameName;
    private String tagLine;
    private String tier;
    private String position;
    private String preferredChampion;
    private int winStreak;
    private float winRate;
    private float avgKda;
    private int totalGames;
}
