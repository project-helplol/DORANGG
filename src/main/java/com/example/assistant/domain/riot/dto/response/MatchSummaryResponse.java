package com.example.assistant.domain.riot.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MatchSummaryResponse {

    private List<MatchResponse> matchs;
    private double winRate;
}
