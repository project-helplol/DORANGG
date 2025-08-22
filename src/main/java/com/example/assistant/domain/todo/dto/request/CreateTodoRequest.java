package com.example.assistant.domain.todo.dto.request;

import com.example.assistant.domain.todo.enums.TodoStatus;
import lombok.Data;

import java.util.List;

@Data
public class CreateTodoRequest {
    private String title;
    private String content;
    private TodoStatus status;
    private List<CreateTodoGoalRequest> goals;
}