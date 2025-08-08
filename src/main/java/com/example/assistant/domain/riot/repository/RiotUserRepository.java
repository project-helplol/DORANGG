package com.example.assistant.domain.riot.repository;

import com.example.assistant.domain.riot.entity.RiotUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiotUserRepository extends JpaRepository<RiotUser, Long> {
    Optional<RiotUser> findByPuuid(String puuid);
    Optional<RiotUser> findByGameNameAndTagLine(String gameName, String tagLine);
}
