package com.example.assistant.domain.riot.repository;

import com.example.assistant.domain.riot.entity.Match;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    @Query
    List<Match> findTopBy20ByRiotIdAndTagline(@Param("riotId") String riotId, @Param("tagline") String tagline, Pageable pageable);
}