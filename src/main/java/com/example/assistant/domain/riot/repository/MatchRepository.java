package com.example.assistant.domain.riot.repository;

import com.example.assistant.domain.riot.entity.Match;
import com.example.assistant.domain.riot.entity.RiotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    boolean existsByMatchId(String matchId);

    List<Match> findTop20ByRiotUserOrderByMatchDateTimeDesc (RiotUser riotUser);

    @Query("SELECT m.matchId FROM Match m WHERE m.riotUser = :riotUser")
    List<String> findMatchIdsByRiotUser(@Param("riotUser") RiotUser riotUser);


    // List<Match> findAllByRiotUserOrderByMatchDateTimeDesc(RiotUser riotUser);
}
