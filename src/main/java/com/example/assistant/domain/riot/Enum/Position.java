package com.example.assistant.domain.riot.Enum;

import lombok.Getter;

@Getter

public enum Position {
    TOP("탑"),
    JUNGLE("정글"),
    MID("미드"),
    BOTTOM("바텀"),
    SUPPORT("서포터");

    private final String description;

    Position(String description) {
        this.description = description;
    }

}
