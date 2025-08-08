package com.example.assistant.domain.riot.repository;

import com.example.assistant.domain.riot.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    boolean existsByMatchId(String matchId);
}
