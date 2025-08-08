package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.riot.dto.request.MatchRequest;
import com.example.assistant.domain.riot.dto.response.MatchResponse;
import com.example.assistant.domain.riot.dto.response.MatchSummaryResponse;
import com.example.assistant.domain.riot.entity.Match;
import com.example.assistant.domain.riot.entity.RiotUser;
import com.example.assistant.domain.riot.enums.GameResult;
import com.example.assistant.domain.riot.repository.MatchRepository;
import com.example.assistant.domain.riot.repository.RiotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final RiotUserRepository riotUserRepository;
    private final RestTemplate restTemplate;

    @Value("${riot.api.key}")
    private String riotApiKey;

    private static final String MATCH_IDS_URL = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?count=20";
    private static final String MATCH_DETAIL_URL = "https://asia.api.riotgames.com/lol/match/v5/matches/{matchId}";
    private static final String MATCH_TIMELINE_URL = "https://asia.api.riotgames.com/lol/match/v5/matches/{matchId}/timeline";

    public MatchSummaryResponse fetchAndSaveMatches(MatchRequest request) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine (request.getGameName(), request.getTagLine())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을수 없어요.."));

        String puuid = riotUser.getPuuid();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String[]> response = restTemplate.exchange(
                MATCH_IDS_URL,
                HttpMethod.GET,
                entity,
                String[].class,
                puuid
        );

        String[] matchIds = response.getBody();
        List<MatchResponse> resultList = new ArrayList<>();

        int winCount = 0;
        int totalCount = 0;

        for (String matchId : matchIds) {
            if (matchRepository.existsByMatchId(matchId)) continue;

            ResponseEntity<Map> detailRes = restTemplate.exchange(
                    MATCH_DETAIL_URL, //MATCH_IDS_URL,
                    HttpMethod.GET,
                    entity,
                    Map.class,
                    matchId
            );

            Map info = (Map) detailRes.getBody().get("info");
            List<Map<String, Object>> participants = (List<Map<String, Object>>) info.get("participants");

            Map<String, Object> userData = participants.stream()
                    .filter(p -> puuid.equals(p.get("puuid")))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("알수없는 유저에요.."));

            long gameStartTimestamp = ((Number) info.get("gameStartTimestamp")).longValue();
            int gameDuration = ((Number) info.get("gameDuration")).intValue();
            LocalDateTime matchDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(gameStartTimestamp), ZoneId.systemDefault());

            ResponseEntity<Map> timeLineRes = restTemplate.exchange(
                    MATCH_TIMELINE_URL, // MATCH_IDS_URL,
                    HttpMethod.GET,
                    entity,
                    Map.class,
                    matchId
            );

            boolean isWin = (Boolean) userData.get("win");
            if (isWin) {
                winCount++;
            }
            totalCount++;

            Match match = Match.builder()
                    .matchId(matchId)
                    .result((Boolean) userData.get("win") ? GameResult.WIN : GameResult.LOSE)
                    .kda(userData.get("kills") + "/" + userData.get("deaths") + "/" + userData.get("assists"))
                    .championName((String) userData.get("championName"))
                    .gameDuration(gameDuration)
                    .matchDateTime(matchDateTime)
                    .riotUser(riotUser)
                    .teamPosition((String) userData.get("teamPosition"))
                    .build();

            matchRepository.save(match);

            resultList.add(MatchResponse.builder()
                    .matchId(match.getMatchId())
                    .result(match.getResult())
                    .kda(match.getKda())
                    .championName(match.getChampionName())
                    .matchDateTime(match.getMatchDateTime())
                    .gameDuration(match.getGameDuration())
                    .teamPosition(match.getTeamPosition())
                    // .timeline(timeLineRes.getBody())
                    .build());

        }

        double winRate = totalCount > 0 ? (winCount * 100.0 / totalCount) : 0;

        return MatchSummaryResponse.builder()
                .matchs(resultList)
                .winRate(winRate)
                .build();
    }
}
