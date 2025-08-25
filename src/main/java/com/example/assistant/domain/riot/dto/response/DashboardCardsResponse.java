package com.example.assistant.domain.riot.dto.response;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DashboardCardsResponse {
    String position;
    String champion;
    String tier;
    String winRate;
    String mastery;
    String streak;
}
