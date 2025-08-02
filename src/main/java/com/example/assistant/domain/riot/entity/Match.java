package com.example.assistant.domain.riot.entity;

import com.example.assistant.domain.riot.Enum.GameResult;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Match")
@Getter
@NoArgsConstructor

public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private RiotUser userid;

    @Column(nullable = false, length = 100)
    private String matchId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameResult result;

    @Column(nullable = false)
    private String kda;

    @Column(length = 30, nullable = false)
    private String champtionName;

    @Column(nullable = false)
    private LocalDateTime playDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private Integer duration;

}
