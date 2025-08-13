package com.example.assistant.domain.riot.repository;

import com.example.assistant.domain.riot.entity.PlayerStats;
import com.example.assistant.domain.riot.entity.RiotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long> {
    Optional<PlayerStats> findByRiotUser (RiotUser riotUser);
    Optional<PlayerStats> findByRiotUser_Id (Long riotUserId);
}
