package com.example.assistant.domain.todo.entity;

import com.example.assistant.domain.todo.enums.TodoStatus;
import jakarta.persistence.*;
import lombok.*;
import com.example.assistant.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String content;

    @Column(nullable = false)
    private String title;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "todo",
    cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoGoal> todoGoals = new ArrayList<>();

    public void addGoal(TodoGoal goal) {
        todoGoals.add(goal);
        goal.setTodo(this);
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;


}