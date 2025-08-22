package com.example.assistant.domain.riot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MatchRequest {
    private String gameName;
    private String tagLine;
}
