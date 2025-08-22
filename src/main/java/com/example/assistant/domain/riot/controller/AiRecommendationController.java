package com.example.assistant.domain.riot.controller;

import com.example.assistant.domain.riot.dto.request.DailyBriefingRequest;
import com.example.assistant.domain.riot.dto.response.AiRecommendationResponse;
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




    @PostMapping(value = "/dashboard-position", consumes = "application/json")
    public ResponseEntity<AiRecommendationResponse> getDashboardPosition(@RequestBody DailyBriefingRequest request) {
        AiRecommendationResponse response = aiRecommendationService.generateDashboardPositionResponse(
                request.getGameName(), request.getTagLine()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/dashboard-champion", consumes = "application/json")
    public ResponseEntity<AiRecommendationResponse> getDashboardChampion(@RequestBody DailyBriefingRequest request) {
        AiRecommendationResponse response = aiRecommendationService.generateDashboardChampionResponse(
                request.getGameName(), request.getTagLine()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/dashboard-tier", consumes = "application/json")
    public ResponseEntity<AiRecommendationResponse> generateDashboardTier(@RequestBody DailyBriefingRequest request) {
        AiRecommendationResponse response = aiRecommendationService.generateDashboardTierResponse(
                request.getGameName(), request.getTagLine()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/dashboard-winrate", consumes = "application/json")
    public ResponseEntity<AiRecommendationResponse> generateDashboardWinRate(@RequestBody DailyBriefingRequest request) {
        AiRecommendationResponse response = aiRecommendationService.generateDashboardWinRateResponse(
                request.getGameName(), request.getTagLine()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/dashboard-mastery", consumes = "application/json")
    public ResponseEntity<AiRecommendationResponse> getDashboardMastery(@RequestBody DailyBriefingRequest request) {
        AiRecommendationResponse response = aiRecommendationService.generateDashboardMasteryResponse(
                request.getGameName(), request.getTagLine()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/dashboard-streak", consumes = "application/json")
    public ResponseEntity<AiRecommendationResponse> getDashboardStreak(@RequestBody DailyBriefingRequest request) {
        AiRecommendationResponse response = aiRecommendationService.generateDashboardStreakResponse(
                request.getGameName(), request.getTagLine()
        );
        return ResponseEntity.ok(response);
    }
}