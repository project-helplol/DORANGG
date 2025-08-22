package com.example.assistant.domain.todo.entity;

import com.example.assistant.domain.todo.enums.TodoStatus;
import jakarta.persistence.*;
import lombok.*;
import com.example.assistant.domain.member.entity.Member;

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

    @Column(nullable = false)
    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;


    // @Column
    // private LocalDateTime endDatetime;

}
