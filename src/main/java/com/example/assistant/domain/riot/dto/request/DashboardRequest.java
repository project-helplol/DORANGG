package com.example.assistant.domain.riot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardRequest {
    private String gameName;
    private String tagLine;
}