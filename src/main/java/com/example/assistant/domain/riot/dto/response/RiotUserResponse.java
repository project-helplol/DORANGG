package com.example.assistant.domain.riot.dto.response;

import com.example.assistant.domain.riot.Enum.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class RiotUserResponse {
    private String riotId;
    private String tagline;
    private String puuid;
    private Tier tier;
    private LocalDateTime createdAt;
}
