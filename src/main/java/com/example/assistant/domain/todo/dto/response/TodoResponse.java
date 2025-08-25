package com.example.assistant.domain.todo.dto.response;

import com.example.assistant.domain.todo.entity.Todo;
import com.example.assistant.domain.todo.enums.TodoStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TodoResponse {
    private Long id;
    private String title;
    private String content;
    private TodoStatus status;
    private List<TodoGoalResponse> goals;

    public static TodoResponse todo(Todo todo){
        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .content(todo.getContent())
                .status(todo.getStatus())
                .goals(todo.getTodoGoals().stream()
                        .map(TodoGoalResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}