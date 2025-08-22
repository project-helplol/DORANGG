package com.example.assistant.domain.riot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiRecommendationResponse {
    private String message;

    public static AiRecommendationResponse of(String message) {
        if (message != null && message.startsWith("\"") && message.endsWith("\"")) {
            message = message.substring(1, message.length() - 1);
        }
        return new AiRecommendationResponse(message);
        }
    }
