package com.example.assistant.domain.riot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "player_stats")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "riot_user_id", nullable = false, unique = true)
    private RiotUser riotUser;

    @Column(nullable = false)
    private double winRate;

    @Column(nullable = false)
    private String favoritePosition;

    @Column(nullable = false)
    private String mostPlayedChampion;

    @Column(nullable = true)
    private String highestMasteryChampion;

    @Column(nullable = true)
    private String tier;

    @Column(nullable = false)
    private int totalGames;

    @Column(nullable = false)
    private int winCount;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = true)
    private LocalDateTime updatedAt;
}


