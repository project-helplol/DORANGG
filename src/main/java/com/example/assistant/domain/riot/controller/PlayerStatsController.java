package com.example.assistant.domain.riot.controller;

import com.example.assistant.domain.riot.dto.request.PlayerStatsRequest;
import com.example.assistant.domain.riot.dto.response.PlayerStatsResponse;
import com.example.assistant.domain.riot.service.PlayerStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player-stats")
@RequiredArgsConstructor
public class PlayerStatsController {
    private final PlayerStatsService playerStatsService;

    @PostMapping
    public PlayerStatsResponse getPlayerStats(@RequestBody PlayerStatsRequest playerStatsRequest) {
        return playerStatsService.getPlayerStats(playerStatsRequest.getGameName(), playerStatsRequest.getTagLine());
    }



}
