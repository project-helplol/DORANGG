package com.example.assistant.domain.riot.controller;

import com.example.assistant.domain.riot.dto.request.DailyBriefingRequest;
import com.example.assistant.domain.riot.service.AiCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiCommentController {

    private final AiCommentService aiCommentService;

    @PostMapping("/daily-briefing")
    public ResponseEntity<String> getDailyBriefing(@RequestBody DailyBriefingRequest request) {
        String comment = aiCommentService.generateDailyBriefing(request);
        return ResponseEntity.ok(comment);
    }
}