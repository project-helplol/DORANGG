package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.riot.dto.gpt.ChatMessage;
import com.example.assistant.domain.riot.dto.gpt.ChatRequest;
import com.example.assistant.domain.riot.dto.gpt.ChatResponse;
import com.example.assistant.domain.riot.dto.request.DailyBriefingRequest;
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


    public String generateDailyBriefing(DailyBriefingRequest request) {
        String prompt = String.format(
                "%s#%s님은 %s 티어의 %s 포지션 유저입니다. 선호 챔피언은 %s이고, 최근 %d연승 중이며 평균 KDA는 %.2f, 승률은 %.2f%%, 총 %d판을 플레이했습니다. 이에 대해 전문가처럼 요약 멘트를 주세요.",
                request.getGameName(), request.getTagLine(), request.getTier(),
                request.getPosition(), request.getPreferredChampion(),
                request.getWinStreak(), request.getAvgKda(), request.getWinRate(), request.getTotalGames()
        );

        ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", Collections.singletonList(new ChatMessage("user", prompt)));

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
