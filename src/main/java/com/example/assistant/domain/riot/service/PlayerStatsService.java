package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.riot.dto.response.PlayerStatsResponse;
import com.example.assistant.domain.riot.entity.Match;
import com.example.assistant.domain.riot.entity.PlayerStats;
import com.example.assistant.domain.riot.entity.RiotUser;
import com.example.assistant.domain.riot.repository.MatchRepository;
import com.example.assistant.domain.riot.repository.PlayerStatsRepository;
import com.example.assistant.domain.riot.repository.RiotUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerStatsService {

    private final RiotUserRepository riotUserRepository;
    private final MatchRepository matchRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final RestTemplate restTemplate;

    @Value("${riot.api.key}")
    private String riotApiKey;

    private Map<String, String> championIdToNameMap;

    @PostConstruct
    public void initChampionMapping() {
        String url = "http://ddragon.leagueoflegends.com/cdn/13.15.1/data/en_US/champion.json";

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> data = (Map<String, Object>) response.get("data");

        championIdToNameMap = new HashMap<>();
        for (String champKey : data.keySet()) {
            Map<String, Object> champInfo = (Map<String, Object>) data.get(champKey);
            String key = (String) champInfo.get("key");
            String name = (String) champInfo.get("id");
            championIdToNameMap.put(key, name);
        }
    }

    private String getChampionNameById(String championId) {
        if (championIdToNameMap == null) return championId;
        return championIdToNameMap.getOrDefault(championId, championId);
    }

    private static final String TIER_BY_PUUID_URL = "https://kr.api.riotgames.com/lol/league/v4/entries/by-puuid/{puuid}";
    private static final String CHAMPION_MASTERY_URL = "https://kr.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/{puuid}";

    public PlayerStatsResponse getPlayerStats (String gameName, String tagLine){
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException("없는 사용자에요.."));

        String puuid = riotUser.getPuuid();

        List<Match> recentMatches = matchRepository.findTop20ByRiotUserOrderByMatchDateTimeDesc(riotUser);

        long winCount = recentMatches.stream()
                .filter(m -> m.getResult().name().equals("WIN"))
                .count();
        double winRate = recentMatches.size() > 0 ? (winCount * 100.0 / recentMatches.size()) : 0.0;

        // 자주 하는 챔피언
        Map<String, Long> championCountMap = recentMatches.stream()
                .collect(Collectors.groupingBy(Match::getChampionName, Collectors.counting()));
        String mostPlayedChampion = championCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);



        Map<String, Long> positionCountMap = recentMatches.stream()
                .collect(Collectors.groupingBy(Match::getTeamPosition, Collectors.counting()));
        String favoritePosition = positionCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (favoritePosition == null || favoritePosition.isBlank()) {
            favoritePosition = "Unknown";
        }

        String tier = fetchTierByPuuid(puuid);

        List<Map<String, Object>> masteryList = fetchChampionMasteryList(puuid);

        String topChampion = null;
        List<String> top3Champions = List.of();

        if (masteryList != null && !masteryList.isEmpty()) {
            Map<String, Object> topChampionData = masteryList.get(0);
            String champId = topChampionData.get("championId").toString();
            String champLevel = topChampionData.get("championLevel").toString();
            topChampion = getChampionNameById(champId) + ":" + champLevel;

            top3Champions = masteryList.stream()
                    .limit(3)
                    .map(m -> getChampionNameById(m.get("championId").toString()))
                    .collect(Collectors.toList());
        }

        PlayerStats playerStats = playerStatsRepository.findByRiotUser(riotUser)
                .orElse(PlayerStats.builder().riotUser(riotUser).build());

        playerStats.setWinRate(winRate);
        playerStats.setFavoritePosition(favoritePosition);
        playerStats.setMostPlayedChampion(mostPlayedChampion);
        playerStats.setHighestMasteryChampion(topChampion);
        playerStats.setTier(tier);
        playerStats.setTotalGames(recentMatches.size());
        playerStats.setWinCount((int) winCount);

        playerStatsRepository.save(playerStats);

        return PlayerStatsResponse.builder()
                .tier(tier)
                .topChampion(topChampion)
                .top3Champions(top3Champions)
                .winRate(winRate)
                .favoritePosition(favoritePosition)
                .mostPlayedChampion(mostPlayedChampion)
                .build();

    }
    private String fetchTierByPuuid(String puuid) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                TIER_BY_PUUID_URL,
                HttpMethod.GET,
                entity,
                List.class,
                puuid
        );
        List<Map<String, Objects>> body = response.getBody();

        if (body == null || body.isEmpty()) {
            return "Unranked";
        }

        // 개인
        return body.stream()
                .filter(entry -> "RANKED_SOLO_5X5".equals(entry.get("queueType")))
                .map(entry -> entry.get("tier") + " " + entry.get("rank"))
                .findFirst()
                .orElseGet(() -> body.get(0).get("tier") + " " + body.get(0).get("rank"))
                .toString();
     }

    private List<Map<String, Object>> fetchChampionMasteryList(String puuid) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                CHAMPION_MASTERY_URL,
                HttpMethod.GET,
                entity,
                List.class,
                puuid
        );

         List<Map<String, Object>> body = response.getBody();

         if (body == null || body.isEmpty()) {
             return null;
         }

         return body;
     }
}
