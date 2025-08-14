package com.example.assistant.domain.ai.client;

import com.example.assistant.domain.ai.dto.message.ChatMessage;
import com.example.assistant.domain.ai.dto.request.ChatRequest;
import com.example.assistant.domain.ai.dto.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SpringAiOpenAiClient {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.model}")
    private String modelName;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendMessage(String prompt) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel(modelName);
        chatRequest.setMessages(Collections.singletonList(new ChatMessage("user", prompt)));

        org.springframework.http.HttpHeaders headers = new HttpHeaders();
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
        if (response.getBody() == null || response.getBody().getChoices().isEmpty()) {
            throw new RuntimeException("AI 응답이 없습니다.");
        }

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
}
