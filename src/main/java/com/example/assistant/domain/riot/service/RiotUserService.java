package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.riot.dto.request.RiotUserRequest;
import com.example.assistant.domain.riot.dto.response.RiotAccountResponse;
import com.example.assistant.domain.riot.dto.response.RiotUserResponse;
import com.example.assistant.domain.riot.entity.RiotUser;
import com.example.assistant.domain.riot.repository.RiotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class RiotUserService {
    private final RiotUserRepository riotUserRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${riot.api.key}")
    private String riotApiKey;

    private static final String[] RIOT_REGIONS = {
            "kr", "na1", "euw1", "eun1", "jp1", "br1", "oce1", "la1", "la2", "ru", "tr1"
    };

    private static final String RIOT_API_URL = "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id";

    public RiotUserResponse register(RiotUserRequest request) {
        String puuid = fetchPuuidFromRiot(request.getGameName(), request.getTagLine());

        RiotUser user = RiotUser.builder()
                .gameName(request.getGameName())
                .tagLine(request.getTagLine())
                .puuid(puuid)
                .build();

        RiotUser saved = riotUserRepository.save(user);
        return RiotUserResponse.from(saved);
    }

    private String fetchPuuidFromRiot(String gameName, String tagLine) {
        String urlTemplate = RIOT_API_URL + "/{gameName}/{tagLine}";

        String url = UriComponentsBuilder
                .fromHttpUrl(urlTemplate)
                .queryParam("api_key", riotApiKey)
                .buildAndExpand(gameName, tagLine)
                .toUriString();

        RiotAccountResponse response = restTemplate.getForObject(url, RiotAccountResponse.class);
        if (response == null || response.getPuuid() == null) {
            throw new IllegalArgumentException("오류 발생! API에서 불러오지 못했어요..");
        }
        return response.getPuuid();
    }
    public RiotUserResponse getByPuuid(String puuid) {
        RiotUser user = riotUserRepository.findByPuuid(puuid)
                .orElseThrow(() -> new IllegalArgumentException("없는 사용자에요.."));
        return RiotUserResponse.from(user);
    }

}


