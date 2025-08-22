package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.riot.dto.request.MatchRequest;
import com.example.assistant.domain.riot.dto.request.RiotUserRequest;
import com.example.assistant.domain.riot.dto.response.*;
import com.example.assistant.domain.riot.enums.Position;
import com.example.assistant.domain.riot.enums.Tier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RiotUserService riotUserService;
    private final MatchService matchService;
    private final PlayerStatsService playerStatsService;
    private final AiRecommendationService aiRecommendationService;

    public DashboardResponse getDashboard(String gameName, String tagLine) {
        RiotUserResponse riotUser = riotUserService.register(
                new RiotUserRequest(gameName, tagLine)
        );

        List<MatchResponse> recentMatches = matchService.fetchAndSaveMatches(
                new MatchRequest(gameName, tagLine)
        );

        PlayerStatsResponse playerStats = playerStatsService.getPlayerStats(gameName, tagLine);

        PlayerStatsResponse dashboardStats = PlayerStatsResponse.builder()
                .tier(convertTier(playerStats.getTier()))
                .topChampion(playerStats.getTopChampion())
                .top3Champions(playerStats.getTop3Champions())
                .winRate(playerStats.getWinRate())
                .favoritePosition(Position.valueOf(playerStats.getFavoritePosition()).getDescription())
                .mostPlayedChampion(playerStats.getMostPlayedChampion())
                .profileIconId(playerStats.getProfileIconId())
                .summonerLevel(playerStats.getSummonerLevel())
                .build();

        String content = aiRecommendationService.generateDailyBriefing(gameName, tagLine);
        content = content.replaceAll("^\"|\"$", "");
        AiRecommendationResponse aiRecommendation = new AiRecommendationResponse(content);

        int currentStreak = calculateCurrentStreak(recentMatches);

        return DashboardResponse.builder()
                .riotUser(riotUser)
                .recentMatches(recentMatches)
                .playerStats(dashboardStats)
                .aiRecommendation(aiRecommendation)
                .currentStreak(currentStreak)
                .build();

    }
    private String convertTier(String rawTier) {
        String[] parts = rawTier.split(" ");
        String baseTier = parts[0];
        String rank = parts.length > 1 ? parts[1] : "";

        String tierName = Tier.valueOf(baseTier).getDescription();
        return rank.isEmpty() ? tierName : tierName + " " + rank;
    }
    private int calculateCurrentStreak(List<MatchResponse> recentMatches) {
        if (recentMatches.isEmpty()) return 0;

        int streak = 0;
        boolean isWin = recentMatches.get(0).getResult().equals("승리");

        for (MatchResponse match : recentMatches) {
            boolean matchWin = match.getResult().equals("승리");
            if (matchWin == isWin) {
                streak += 1;
            } else {
                break;
            }
        }

        return isWin ? streak : -streak;
    }
}
