package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.riot.dto.response.RiotUserResponse;
import com.example.assistant.domain.riot.entity.RiotUser;
import com.example.assistant.domain.riot.repository.RiotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RiotUserService {
    private final RiotUserRepository riotUserRepository;

    public RiotUserResponse findByRiotIdAndTagline(String riotId, String tagline)  {
        RiotUser user = riotUserRepository.findByRiotIdAndTagline(riotId, tagline)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return RiotUserResponse.builder()
                .riotId(user.getRiotId())
                .tagline(user.getTagline())
                .puuid(user.getPuuid())
                .tier(user.getTier())
                .createdAt(user.getCreatedAt())
                .build();

    }
}

