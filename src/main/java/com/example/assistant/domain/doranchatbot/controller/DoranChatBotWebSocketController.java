package com.example.assistant.domain.doranchatbot.controller;

import com.example.assistant.domain.doranchatbot.dto.request.DoranChatBotRequest;
import com.example.assistant.domain.doranchatbot.dto.response.DoranChatBotResponse;
import com.example.assistant.domain.doranchatbot.service.DoranChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DoranChatBotWebSocketController {

    private final DoranChatBotService doranChatBotService;

    @MessageMapping("/doranchatbot")
    @SendTo("/topic/doranchatbot")
    public DoranChatBotResponse chat (DoranChatBotRequest request) {
        if (request.getPrompt() != null && request.getPrompt().length() > 100) {
            return new DoranChatBotResponse("100글자 이내로 입력해주세요.");
        }
        String responseMessage = doranChatBotService.generateResponse(
                request.getGameName(),
                request.getTagLine(),
                request.getPrompt()
        );

        System.out.println("[DoranChatBot] 요청: " + request.getPrompt());
        System.out.println("[DoranChatBot] 응답: " + responseMessage);

        return new DoranChatBotResponse(responseMessage);
    }
}
