package com.example.assistant.domain.riot.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
    private String model;
    private List<ChatMessage> messages;

    // 다양한 말투
    private Double temperature = 0.7;
    // 다양한 단어선택
    private Double top_p = 0.9;
    // 새로운 주제, 단어
    private Double presence_penalty = 0.3;
    // 동일 단어 반복
    private Double frequency_penalty = 0.2;
}
