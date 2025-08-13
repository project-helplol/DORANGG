package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.riot.dto.gpt.ChatMessage;
import com.example.assistant.domain.riot.dto.gpt.ChatRequest;
import com.example.assistant.domain.riot.dto.gpt.ChatResponse;
import com.example.assistant.domain.riot.dto.request.DailyBriefingRequest;
import com.example.assistant.domain.riot.entity.PlayerStats;
import com.example.assistant.domain.riot.entity.RiotUser;
import com.example.assistant.domain.riot.repository.PlayerStatsRepository;
import com.example.assistant.domain.riot.repository.RiotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AiCommentService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.model}")
    private String modelName;

    private final RiotUserRepository riotUserRepository;
    private final PlayerStatsRepository playerStatsRepository;


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

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel(modelName);
        chatRequest.setMessages(Collections.singletonList(new ChatMessage("user", prompt)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(chatRequest, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ChatResponse> response = restTemplate.exchange(
                "https://api.openai.com/v1/chat/completions",
                HttpMethod.POST,
                entity,
                ChatResponse.class
        );

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
    }
