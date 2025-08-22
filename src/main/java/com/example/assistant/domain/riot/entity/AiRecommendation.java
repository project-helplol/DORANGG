package com.example.assistant.domain.riot.entity;

import com.example.assistant.domain.riot.enums.RecommendationType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(
        name = "ai_comment",
        indexes = {
                @Index(name = "idx_ai_comment_user_type_created", columnList = "riot_user_id, created_at"),
                @Index(name = "idx_ai_comment_user_type_hash", columnList = "riot_user_id, source_hash")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AiRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "riot_user_id", nullable = false)
    private RiotUser riotUser;


    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationType type;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(name = "source_hash", length = 64)
    private String sourceHash;

    @Column(name = "source_stats_updated_at")
    private LocalDateTime sourceStatsUpdatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
