package com.example.assistant.domain.riot.entity;

import com.example.assistant.domain.riot.enums.GameResult;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "match-info")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "riot_user_id", nullable = false)
    private RiotUser riotUser;

    @Column(unique = true, nullable = false)
    private String matchId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameResult result;

    @Column(nullable = false)
    private String kda;

    @Column(nullable = false) // 변경요망 테스트중
    private String championName;

    @Column(nullable = false)
    private LocalDateTime matchDateTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int gameDuration;

    @Column(nullable = false)
    private String teamPosition;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
