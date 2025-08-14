package com.example.assistant.domain.doranchatbot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoranChatBotRequest {
    private String gameName;
    private String tagLine;
    private String prompt;
}
