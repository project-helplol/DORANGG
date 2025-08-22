package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.riot.dto.request.MatchRequest;
import com.example.assistant.domain.riot.dto.response.MatchResponse;
import com.example.assistant.domain.riot.entity.Match;
import com.example.assistant.domain.riot.entity.RiotUser;
import com.example.assistant.domain.riot.enums.GameResult;
import com.example.assistant.domain.riot.repository.MatchRepository;
import com.example.assistant.domain.riot.repository.RiotUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MatchService {

    private final MatchRepository matchRepository;
    private final RiotUserRepository riotUserRepository;
    private final RestTemplate restTemplate;

    @Value("${riot.api.key}")
    private String riotApiKey;

    private static final String MATCH_IDS_URL = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?count=20";
    private static final String MATCH_DETAIL_URL = "https://asia.api.riotgames.com/lol/match/v5/matches/{matchId}";
    private static final String MATCH_TIMELINE_URL = "https://asia.api.riotgames.com/lol/match/v5/matches/{matchId}/timeline";

    private String getGameType(int queueId) {
        return switch(queueId) {
            case 420 -> "RANKED";
            case 430, 440 -> "NORMAL";
            case 450 -> "ARAM";
            default -> "OTHER";
        };
    }

    private <T> ResponseEntity<T> safeExchange(String url, HttpMethod method, HttpEntity<?> entity, Class<T> responseType, Object... uriVariables) {
        int maxRetry = 3;

        for (int i = 0; i < maxRetry; i++) {
            try {
                if (i > 0) log.warn("재시도 중... {}", (maxRetry - i));
                return restTemplate.exchange(url, method, entity, responseType, uriVariables);
            } catch (Exception e) {
                if (i == maxRetry - 1) {
                    throw e;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ex);
                }
            }
        }
        throw new RuntimeException("재시도 실패");
    }

    public List<MatchResponse> fetchAndSaveMatches(MatchRequest request) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(request.getGameName(), request.getTagLine())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을수 없어요.."));

        String puuid = riotUser.getPuuid();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String[]> response = safeExchange(
                MATCH_IDS_URL,
                HttpMethod.GET,
                entity,
                String[].class,
                puuid
        );

        String[] matchIds = response.getBody();
        List<MatchResponse> resultList = new ArrayList<>();

        List<String> existingMatchIds = matchRepository.findMatchIdsByRiotUser(riotUser);

        for (String matchId : matchIds) {
            if (existingMatchIds.contains(matchId)) continue;

            ResponseEntity<Map> detailRes = safeExchange(
                    MATCH_DETAIL_URL,
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

            int queueId = ((Number) info.get("queueId")).intValue();
            String gameType = getGameType(queueId);

            ResponseEntity<Map> timeLineRes = safeExchange(
                    MATCH_TIMELINE_URL,
                    HttpMethod.GET,
                    entity,
                    Map.class,
                    matchId
            );

            Match match = Match.builder()
                    .matchId(matchId)
                    .result((Boolean) userData.get("win") ? GameResult.WIN : GameResult.LOSE)
                    .kda(userData.get("kills") + "/" + userData.get("deaths") + "/" + userData.get("assists"))
                    .championName((String) userData.get("championName"))
                    .gameDuration(gameDuration)
                    .matchDateTime(matchDateTime)
                    .riotUser(riotUser)
                    .teamPosition((String) userData.get("teamPosition"))
                    .gameType(gameType)
                    .queueId(queueId)
                    .build();

            matchRepository.save(match);

            resultList.add(MatchResponse.builder()
                    .matchId(match.getMatchId())
                    .result(match.getResult().getDescription())
                    .kda(match.getKda())
                    .championName(match.getChampionName())
                    .matchDateTime(match.getMatchDateTime())
                    .gameDuration(match.getGameDuration())
                    .teamPosition(match.getTeamPosition())
                    .gameType(gameType)
                    .queueId(queueId)
                    // .timeline(timeLineRes.getBody())
                    .build());

        }
        resultList.clear();

        List<Match> recentMatches = matchRepository.findTop20ByRiotUserOrderByMatchDateTimeDesc(riotUser);

        for (Match m : recentMatches) {
            resultList.add(MatchResponse.builder()
                    .matchId(m.getMatchId())
                    .result(m.getResult().getDescription())
                    .kda(m.getKda())
                    .championName(m.getChampionName())
                    .matchDateTime(m.getMatchDateTime())
                    .gameDuration(m.getGameDuration())
                    .teamPosition(m.getTeamPosition())
                    .gameType(m.getGameType())
                    .queueId(m.getQueueId())
                    .build());



            // DB에 저장된 매치가 20개를 초과할 경우,
            // 오래된 매치 데이터를 삭제하여 저장 개수를 제한하는 로직 (추후 구현 예정)
            // 1. 해당 유저의 모든 매치를 최신순으로 조회
            // 2. 20번째 이후의 매치들을 잘라서 삭제 대상 리스트 생성
            // 3. 삭제 대상 리스트를 DB에서 일괄 삭제
//            List<Match> allMatches = matchRepository.findAllByRiotUserOrderByMatchDateTimeDesc(riotUser);
//            if (allMatches.size() > 20) {
//                List<Match> toDelete = allMatches.subList(20, allMatches.size());
//                matchRepository.deleteAll(toDelete);
            }
        return resultList;
        }

    }