package com.example.assistant.domain.riot.repository;

import com.example.assistant.domain.riot.entity.AiRecommendation;
import com.example.assistant.domain.riot.entity.RiotUser;
import com.example.assistant.domain.riot.enums.RecommendationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;


public interface AiRecommendationRepository extends JpaRepository<AiRecommendation, Long> {

    Optional<AiRecommendation> findTopByRiotUserAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
            RiotUser riotUser,
            RecommendationType type,
            LocalDateTime start,
            LocalDateTime end
    );

    Optional<AiRecommendation> findTopByRiotUserAndTypeOrderByCreatedAtDesc(
            RiotUser riotUser, RecommendationType type
    );

}
