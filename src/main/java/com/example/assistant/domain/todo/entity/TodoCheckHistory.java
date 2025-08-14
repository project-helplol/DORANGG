package com.example.assistant.domain.todo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo_check_history")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoCheckHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "todo_id", nullable = false)
    private Long todoId;

    @Column(name = "checked_by", length = 100, nullable = false)
    private String checkedBy;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "previous_state", nullable = false)
    private boolean previousState;

    @Column(name = "source_data", columnDefinition = "TEXT")
    private  String sourceData;

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt;


}
