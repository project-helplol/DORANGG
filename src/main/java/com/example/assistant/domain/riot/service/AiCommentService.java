package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.ai.client.SpringAiOpenAiClient;
import com.example.assistant.domain.riot.entity.PlayerStats;
import com.example.assistant.domain.riot.entity.RiotUser;
import com.example.assistant.domain.riot.repository.PlayerStatsRepository;
import com.example.assistant.domain.riot.repository.RiotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiCommentService {
    
    private final RiotUserRepository riotUserRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final SpringAiOpenAiClient springAiOpenAiClient;

    public String generateDailyBriefing(String gameName, String tagLine) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)
                ));

        PlayerStats stats = playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())
                ));

        String prompt = String.format(
                "LoL 전적 분석가이자 멘탈 코치로서 %s#%s (%s, 승률 %.1f%%)에게 한 줄 멘트.",
                stats.getRiotUser().getGameName(),
                stats.getRiotUser().getTagLine(),
                stats.getFavoritePosition(),
                stats.getWinRate()
        );
        return springAiOpenAiClient.sendMessage(prompt);
    }
    }
