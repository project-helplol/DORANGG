package com.example.assistant.domain.todo.dto.response;

import com.example.assistant.domain.todo.entity.TodoGoal;
import com.example.assistant.domain.todo.enums.GoalStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoGoalResponse {
    private Long id;
    private String content;
    private GoalStatus status;

    public static TodoGoalResponse from(TodoGoal goal) {
        return TodoGoalResponse.builder()
                .id(goal.getId())
                .content(goal.getContent())
                .status(goal.getStatus())
                .build();
    }
}
