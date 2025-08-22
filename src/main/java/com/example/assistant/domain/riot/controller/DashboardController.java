package com.example.assistant.domain.riot.controller;

import com.example.assistant.domain.riot.dto.request.DashboardRequest;
import com.example.assistant.domain.riot.dto.response.DashboardResponse;
import com.example.assistant.domain.riot.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping
    public DashboardResponse getDashboard(@RequestBody DashboardRequest request) {
        return dashboardService.getDashboard(request.getGameName(), request.getTagLine());
    }
}
