package com.example.assistant.domain.todo.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoStatusUpdateRequest {
    private Long todoId;
    private String newStatus;
    private String updateBy;
}
