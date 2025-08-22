package com.example.assistant.domain.riot.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DashboardResponse {
    private RiotUserResponse riotUser;
    private PlayerStatsResponse playerStats;
    private List<MatchResponse> recentMatches;
    private AiRecommendationResponse dailyBriefing;
    private AiRecommendationResponse strategyRecommendation;
    private DashboardCardsResponse dashboardCards;
    private int currentStreak;
}
