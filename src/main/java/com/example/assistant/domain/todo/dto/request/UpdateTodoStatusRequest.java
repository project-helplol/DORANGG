package com.example.assistant.domain.todo.dto.request;

import com.example.assistant.domain.todo.enums.GoalStatus;
import com.example.assistant.domain.todo.enums.TodoStatus;
import lombok.Data;

import java.util.Map;

@Data
public class UpdateTodoStatusRequest {
    private TodoStatus status;
    private Map<Long, GoalStatus> goalStatusMap;
}
