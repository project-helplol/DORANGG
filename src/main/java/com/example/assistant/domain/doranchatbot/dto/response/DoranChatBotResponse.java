package com.example.assistant.domain.doranchatbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoranChatBotResponse {
    private String message;
    private String gameName;
    private String tagLine;
}
