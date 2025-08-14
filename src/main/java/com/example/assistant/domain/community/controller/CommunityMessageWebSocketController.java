package com.example.assistant.domain.community.controller;

import com.example.assistant.domain.community.dto.request.CommunityMessageRequest;
import com.example.assistant.domain.community.dto.response.CommunityMessageResponse;
import com.example.assistant.domain.community.service.CommunityMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommunityMessageWebSocketController {

    private final CommunityMessageService communityMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/community")
    public void sendMessage(@Payload CommunityMessageRequest request, Principal principal) {

        if (!communityMessageService.exists(request.getRoom())) {
            throw new IllegalArgumentException("유효하지 않은 채팅방입니다: " + request.getRoom());
        }

        Long memberId = Long.parseLong(principal.getName());

        CommunityMessageResponse savedMessage = communityMessageService.saveMessage(memberId, request);

        System.out.println("[WebSocket] 저장 완료: " + savedMessage);

        messagingTemplate.convertAndSend("/topic/community/" + request.getRoom(), savedMessage);

        System.out.println("[WebSocket] 발행 완료: /topic/community/" + request.getRoom());
    }
}
