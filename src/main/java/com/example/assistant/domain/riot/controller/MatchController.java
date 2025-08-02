package com.example.assistant.domain.riot.controller;

import com.example.assistant.domain.riot.dto.response.MatchResponse;
import com.example.assistant.domain.riot.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/recent")
    public ResponseEntity<List<MatchResponse>> getRencntMatchs(
            @RequestParam String riotId,
            @RequestParam String tagline
    ) {
        List<MatchResponse> matches = matchService.getRecentMatches(riotId, tagline);
        return ResponseEntity.ok(matches);
    }

}

