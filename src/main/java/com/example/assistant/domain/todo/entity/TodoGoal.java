package com.example.assistant.domain.todo.entity;

import com.example.assistant.domain.todo.enums.GoalStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private GoalStatus status = GoalStatus.PENDING;
}
