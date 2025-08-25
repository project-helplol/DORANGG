package com.example.assistant.domain.riot.controller;

import com.example.assistant.domain.riot.dto.request.DailyBriefingRequest;
import com.example.assistant.domain.riot.dto.response.AiRecommendationResponse;
import com.example.assistant.domain.riot.dto.response.DashboardCardsResponse;
import com.example.assistant.domain.riot.service.AiRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    @PostMapping(value = "/daily-briefing", consumes = "application/json")
    public ResponseEntity<AiRecommendationResponse> getDailyBriefing(@RequestBody DailyBriefingRequest request) {
        AiRecommendationResponse response = aiRecommendationService.generateDailyBriefingResponse(
                request.getGameName(), request.getTagLine()
        );
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/strategy-briefing", consumes = "application/json")
    public ResponseEntity<AiRecommendationResponse> getStrategyBriefing(@RequestBody DailyBriefingRequest request) {
        AiRecommendationResponse response = aiRecommendationService.generateStrategyBriefingResponse(
                request.getGameName(), request.getTagLine()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/build-briefing", consumes = "application/json")
    public ResponseEntity<AiRecommendationResponse> getBuildBriefing(@RequestBody DailyBriefingRequest request) {
        AiRecommendationResponse response = aiRecommendationService.generateBuildBriefingResponse(
                request.getGameName(), request.getTagLine()
        );
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/dashboard-cards", consumes = "application/json")
    public ResponseEntity<DashboardCardsResponse> getDashboardCards(@RequestBody DailyBriefingRequest request) {
        DashboardCardsResponse response =
                aiRecommendationService.generateDashboardCardsResponse(request.getGameName(), request.getTagLine());
        return ResponseEntity.ok(response);
    }
}