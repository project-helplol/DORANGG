package com.example.assistant.domain.todo.dto.request;

import com.example.assistant.domain.todo.enums.TodoStatus;
import lombok.Data;

@Data
public class UpdateTodoStatusRequest {
    private TodoStatus status;
}
