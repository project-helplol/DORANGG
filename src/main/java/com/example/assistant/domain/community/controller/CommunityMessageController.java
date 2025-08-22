package com.example.assistant.domain.community.controller;

import com.example.assistant.domain.community.dto.request.CommunityMessageRequest;
import com.example.assistant.domain.community.dto.response.CommunityMessageResponse;
import com.example.assistant.domain.community.service.CommunityMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/messages")
@RequiredArgsConstructor
public class CommunityMessageController {

    private final CommunityMessageService communityMessageService;

    @PostMapping
    public ResponseEntity<CommunityMessageResponse> postMessage(
            @AuthenticationPrincipal Long memberId,
            @RequestBody CommunityMessageRequest request
    ) {
        CommunityMessageResponse response = communityMessageService.saveMessage(memberId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CommunityMessageResponse>> getLatestMessages(
            @RequestParam String room
    ) {
        List<CommunityMessageResponse> messages = communityMessageService.getLatestMessages(room);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/previous")
    public ResponseEntity<List<CommunityMessageResponse>> getPreviousMessages(
            @RequestParam String room,
            @RequestParam String before
    ) {
        List<CommunityMessageResponse> messages = communityMessageService.getPreviousMessages(room, before);
        return ResponseEntity.ok(messages);
    }
}