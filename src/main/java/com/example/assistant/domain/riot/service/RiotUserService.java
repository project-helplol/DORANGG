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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class RiotUserService {
    private static final Logger logger = LoggerFactory.getLogger(RiotUserService.class);

    private final RiotUserRepository riotUserRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${riot.api.key}")
    private String riotApiKey;

    // private static final String[] RIOT_REGIONS = {
            // "kr", "na1", "euw1", "eun1", "jp1", "br1", "oce1", "la1", "la2", "ru", "tr1"
    // };

    private static final String RIOT_API_URL = "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id";

    public RiotUserResponse register(RiotUserRequest request) {
        String puuid = fetchPuuidFromRiot(request.getGameName(), request.getTagLine());

        return riotUserRepository.findByGameNameAndTagLine(request.getGameName(), request.getTagLine())
                .map(existingUser -> {
                    if (!puuid.equals(existingUser.getPuuid())) {
                        existingUser.setPuuid(puuid);
                        riotUserRepository.save(existingUser);
                        logger.info("기존 사용자 '{}'의 puuid가 변경되어 업데이트 했습니다.", request.getGameName());
                    } else {
                        logger.info("이미 저장된 유저 '{}'가 존재합니다. 반환만 합니다.", request.getGameName());
                    }
                    return RiotUserResponse.from(existingUser);
                })
                .orElseGet(() -> {
                    RiotUser newUser = RiotUser.builder()
                            .gameName(request.getGameName())
                            .tagLine(request.getTagLine())
                            .puuid(puuid)
                            .build();
                    RiotUser savedUser = riotUserRepository.save(newUser);
                    logger.info("새로운 사용자 '{}' 저장 완료.", request.getGameName());
                    return RiotUserResponse.from(savedUser);
                });
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
