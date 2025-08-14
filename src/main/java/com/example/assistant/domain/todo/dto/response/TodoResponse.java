package com.example.assistant.domain.todo.dto.response;

import com.example.assistant.domain.todo.entity.Todo;
import com.example.assistant.domain.todo.enums.TodoStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoResponse {
    private Long id;
    private String title;
    private String content;
    private TodoStatus status;

    public static TodoResponse from(Todo todo){
        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .content(todo.getContent())
                .status(todo.getStatus())
                .build();
    }
}
