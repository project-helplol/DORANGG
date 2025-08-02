package com.example.assistant.domain.riot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.assistant.domain.riot.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {
	// List<Match> findTopBy20ByRiotIdAndTagline(@Param("riotId") String riotId, @Param("tagline") String tagline,
	// 	Pageable pageable);
}
