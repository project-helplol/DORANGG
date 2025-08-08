package com.example.assistant.domain.riot.controller;

import com.example.assistant.domain.riot.dto.request.MatchRequest;
import com.example.assistant.domain.riot.dto.response.MatchSummaryResponse;
import com.example.assistant.domain.riot.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/summary")
    public MatchSummaryResponse fetchAndSaveMatches(@RequestBody MatchRequest request) {
        return matchService.fetchAndSaveMatches(request);
    }

}

