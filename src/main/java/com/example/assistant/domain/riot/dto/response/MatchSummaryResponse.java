package com.example.assistant.domain.riot.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MatchSummaryResponse {
    private int recentGameCount;
    private float winRate;
    private int winStreak;
    private int loseStreak;
}
