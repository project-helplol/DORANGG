package com.example.assistant.domain.doranchatbot.controller;

import com.example.assistant.domain.doranchatbot.dto.request.DoranChatBotRequest;
import com.example.assistant.domain.doranchatbot.dto.response.DoranChatBotResponse;
import com.example.assistant.domain.doranchatbot.service.DoranChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DoranChatBotWebSocketController {

    private final DoranChatBotService doranChatBotService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/doranchatbot")
    public void chat(DoranChatBotRequest request) {
        String responseMessage = String.valueOf(doranChatBotService.generateResponse(
                request.getGameName(),
                request.getTagLine(),
                request.getPrompt()
        ));

        DoranChatBotResponse response = new DoranChatBotResponse(
                responseMessage,
                request.getGameName(),
                request.getTagLine()
        );

        // DTO를 그대로 전송
        messagingTemplate.convertAndSend("/topic/doranchatbot", response);

        System.out.println("[DoranChatBot] 요청: " + request.getPrompt());
        System.out.println("[DoranChatBot] 응답: " + responseMessage);
    }
}