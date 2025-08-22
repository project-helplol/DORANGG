package com.example.assistant.domain.todo.enums;

public enum TodoType {
    MANUAL("사용자가 직접 생성"), // 사용자가 직접 생성
    AUTO("AI가 생성) //AI가 생성"); // AI가 생성

    private final String description;
TodoType(String description) {this.description = description; }
}

