package com.example.assistant.domain.doranchatbot.service;

import com.example.assistant.domain.ai.client.SpringAiOpenAiClient;
import com.example.assistant.domain.riot.service.PlayerStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoranChatBotService {

    private final SpringAiOpenAiClient springAiOpenAiClient;
    private final PlayerStatsService playerStatsService;

    public String generateResponse(String gameName, String tagLine, String userPrompt) {

        var stats = playerStatsService.getPlayerStats(gameName, tagLine);

        String systemPrompt = """
            당신은 도란챗봇입니다.
            항상 친절하며, 리그오브레전드 관련 전략, 포지션, 챔피언 추천, 승률 분석에 집중합니다.
            다른 주제에는 답하지 마세요.
            """;

        String finalPrompt = systemPrompt +
                "\n사용자 게임명: " + gameName +
                "\n태그라인: " + tagLine +
                "\n티어: " + stats.getTier() +
                "\n주력 챔피언: " + stats.getTopChampion() +
                "\n상위 3 챔피언: " + stats.getTop3Champions() +
                "\n승률: " + stats.getWinRate() +
                "\n선호 포지션: " + stats.getFavoritePosition() +
                "\n가장 많이 플레이한 챔피언: " + stats.getMostPlayedChampion() +
                "\n질문: " + userPrompt;

        return springAiOpenAiClient.sendMessage(finalPrompt);
    }
}
