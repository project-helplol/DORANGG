package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.ai.client.SpringAiOpenAiClient;
import com.example.assistant.domain.riot.dto.response.AiRecommendationResponse;
import com.example.assistant.domain.riot.entity.AiRecommendation;
import com.example.assistant.domain.riot.entity.PlayerStats;
import com.example.assistant.domain.riot.entity.RiotUser;
import com.example.assistant.domain.riot.enums.RecommendationType;
import com.example.assistant.domain.riot.repository.AiRecommendationRepository;
import com.example.assistant.domain.riot.repository.PlayerStatsRepository;
import com.example.assistant.domain.riot.repository.RiotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private final RiotUserRepository riotUserRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final SpringAiOpenAiClient springAiOpenAiClient;
    private final AiRecommendationRepository aiRecommendationRepository;

    public String generateDailyBriefing(String gameName, String tagLine) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)
                ));

        PlayerStats stats = playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())
                ));

        LocalDate today = LocalDate.now();

        String content = aiRecommendationRepository.findTopByRiotUserAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                        riotUser,
                        RecommendationType.DAILY,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                ).map(AiRecommendation::getContent)
                .orElseGet(() -> {

                    String prompt = String.format(
                            "LoL코치로서 %s#%s (%s, 승률 %.1f%%)님의 최근 플레이를 기반으로 조언해주세요." +
                            "잘하면 칭찬, 부족하면 격려를 친절한 존댓말로 랜덤하게 작성해주세요.(30자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getFavoritePosition(),
                            stats.getWinRate()
                    );

                    String generatedContent = springAiOpenAiClient.sendMessage(prompt);
                    AiRecommendation aiRec = AiRecommendation.builder()
                            .riotUser(riotUser)
                            .type(RecommendationType.DAILY)
                            .content(generatedContent)
                            .createdAt(LocalDateTime.now())
                            .build();
                    aiRecommendationRepository.save(aiRec);

                    return generatedContent;
                });
        return content;

    }

    public AiRecommendationResponse generateDailyBriefingResponse(String gameName, String tagLine) {
        String content = generateDailyBriefing(gameName, tagLine);
        return AiRecommendationResponse.of(content);
    }

    public String generateStrategyBriefing0(String gameName, String tagLine) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)
                ));

        PlayerStats stats = playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())
                ));

        LocalDate today = LocalDate.now();

        String content = aiRecommendationRepository.findTopByRiotUserAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                        riotUser,
                        RecommendationType.STRATEGY,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                )
                .map(AiRecommendation::getContent)
                .orElseGet(() -> {
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s (%s, 승률 %.1f%%)님의 최근 플레이를 기반으로 전략분석을 해주세요." +
                            "인사말, 소환사 명 출력 금지, 본문만 작성해주세요." +
                            "친절한 존댓말로 랜덤하게 작성하며, 부족한 부분은 개선점을 짚어주시고 잘한 부분은 칭찬해주세요. (200자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getFavoritePosition(),
                            stats.getWinRate()
                    );
                    String generatedContent = springAiOpenAiClient.sendMessage(prompt);

                    AiRecommendation aiRec = AiRecommendation.builder()
                            .riotUser(riotUser)
                            .type(RecommendationType.STRATEGY)
                            .content(generatedContent)
                            .createdAt(LocalDateTime.now())
                            .build();
                    aiRecommendationRepository.save(aiRec);
                    return generatedContent;
                });
        return content;
    }

    public AiRecommendationResponse generateStrategyBriefingResponse(String gameName, String tagLine) {
        String content = generateStrategyBriefing0(gameName, tagLine);
        return AiRecommendationResponse.of(content);
    }


    public String generateBuildBriefing(String gameName, String tagLine) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)
                ));

        PlayerStats stats = playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())
                ));

        LocalDate today = LocalDate.now();

        String content = aiRecommendationRepository.findTopByRiotUserAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                        riotUser,
                        RecommendationType.BUILD,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                )
                .map(AiRecommendation::getContent)
                .orElseGet(() -> {
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s (%s, 승률 %.1f%%)님의 최근 플레이를 기반으로 빌드분석을 해주세요."+
                            "인사말, 소환사 명 출력 금지, 본문만 작성해주세요." +
                            "코어템, 핵심룬, 스펠 기준 중심으로 친절한 존댓말로 랜덤하게 설명해주세요.(200자 이내)" ,
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getFavoritePosition(),
                            stats.getWinRate()
                    );
                    String generatedContent = springAiOpenAiClient.sendMessage(prompt);

                    AiRecommendation aiRec = AiRecommendation.builder()
                            .riotUser(riotUser)
                            .type(RecommendationType.BUILD)
                            .content(generatedContent)
                            .createdAt(LocalDateTime.now())
                            .build();
                    aiRecommendationRepository.save(aiRec);
                    return generatedContent;
                });
        return content;
    }

    public AiRecommendationResponse generateBuildBriefingResponse(String gameName, String tagLine) {
        String content = generateBuildBriefing(gameName, tagLine);
        return AiRecommendationResponse.of(content);
    }

    public String generateDashboardPosition(String gameName, String tagLine) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)
                ));

        PlayerStats stats = playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())
                ));

        LocalDate today = LocalDate.now();

        String content = aiRecommendationRepository.findTopByRiotUserAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                        riotUser,
                        RecommendationType.DASHBOARD_POSITION,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                )
                .map(AiRecommendation::getContent)
                .orElseGet(() -> {
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s의 최근 포지션별 승률을 반드시 두 줄로 작성하며 형식은 고정입니다. " +
                            "1줄: ‘최근 [포지션명] 승률은 [수치]%% [상승/하락]했으나 [▲/▼]’ " +
                            "2줄: ‘[포지션명] 승률은 [수치]%% [상승/하락]했습니다 [▲/▼]’ " +
                            "▲는 상승, ▼는 하락일 때만 사용하세요.",
                            riotUser.getGameName(),
                            riotUser.getTagLine()
                    );
                    String generatedContent = springAiOpenAiClient.sendMessage(prompt);
                    AiRecommendation aiRec = AiRecommendation.builder()
                            .riotUser(riotUser)
                            .type(RecommendationType.DASHBOARD_POSITION)
                            .content(generatedContent)
                            .createdAt(LocalDateTime.now())
                            .build();
                    aiRecommendationRepository.save(aiRec);
                    return generatedContent;
                });
        return content;
    }

    public AiRecommendationResponse generateDashboardPositionResponse(String gameName, String tagLine) {
        String content = generateDashboardPosition(gameName, tagLine);
        return AiRecommendationResponse.of(content);
    }

    public String generateDashboardChampions(String gameName, String tagLine) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)
                ));

        PlayerStats stats = playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())
                ));

        LocalDate today = LocalDate.now();

        String content = aiRecommendationRepository.findTopByRiotUserAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                        riotUser,
                        RecommendationType.DASHBOARD_CHAMPION,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                ).map(AiRecommendation::getContent)
                .orElseGet(() -> {
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s님의 가장 자주 플레이하는 챔피언은 '%s'입니다." +
                            "챔피언에 대해 항상 친절한 존댓말로 간단히 말씀해주세요.(50자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getMostPlayedChampion()
                    );
                    String generated = springAiOpenAiClient.sendMessage(prompt);
                    aiRecommendationRepository.save(AiRecommendation.builder()
                            .riotUser(riotUser)
                            .type(RecommendationType.DASHBOARD_CHAMPION)
                            .content(generated)
                            .createdAt(LocalDateTime.now())
                            .build());
                    return generated;
                });
        return content;
    }

    public AiRecommendationResponse generateDashboardChampionResponse(String gameName, String tagLine) {
        return AiRecommendationResponse.of(generateDashboardChampions(gameName, tagLine));
    }

    public String generateDashboardTier(String gameName, String tagLine) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)
                ));

        PlayerStats stats = playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())
                ));

        LocalDate today = LocalDate.now();

        String content = aiRecommendationRepository.findTopByRiotUserAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                        riotUser,
                        RecommendationType.DASHBOARD_TIER,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                ).map(AiRecommendation::getContent)
                .orElseGet(() -> {
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s의 현재 티어 '%s' 최근 며칠간의 순위 추세(상승/정체/하락)를 요약해주세요, " +
                            "존댓말로 친절하게 작성해주세요(50자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getTier()
                    );
                    String generated = springAiOpenAiClient.sendMessage(prompt);
                    aiRecommendationRepository.save(AiRecommendation.builder()
                            .riotUser(riotUser)
                            .type(RecommendationType.DASHBOARD_TIER)
                            .content(generated)
                            .createdAt(LocalDateTime.now())
                            .build());
                    return generated;
                });
        return content;
    }

    public AiRecommendationResponse generateDashboardTierResponse(String gameName, String tagLine) {
        return AiRecommendationResponse.of(generateDashboardTier(gameName, tagLine));
    }

    public String generateDashboardWinRate(String gameName, String tagLine) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)
                ));

        PlayerStats stats = playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())
                ));

        LocalDate today = LocalDate.now();

        String content = aiRecommendationRepository.findTopByRiotUserAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                        riotUser,
                        RecommendationType.DASHBOARD_WINRATE,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                ).map(AiRecommendation::getContent)
                .orElseGet(() -> {
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s 전체 전적은 %d전 %d승이며 승률은 %.1f%%입니다." +
                            "20판중 몇 판 승리했는지와 패배했는지를 친절한 존댓말로 작성해주세요.(50자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getTotalGames(),
                            stats.getWinCount(),
                            stats.getWinRate()
                    );
                    String generated = springAiOpenAiClient.sendMessage(prompt);
                    aiRecommendationRepository.save(AiRecommendation.builder()
                            .riotUser(riotUser)
                            .type(RecommendationType.DASHBOARD_WINRATE)
                            .content(generated)
                            .createdAt(LocalDateTime.now())
                            .build());
                    return generated;
                });
        return content;
    }

    public AiRecommendationResponse generateDashboardWinRateResponse(String gameName, String tagLine) {
        return AiRecommendationResponse.of(generateDashboardWinRate(gameName, tagLine));
    }

    public String generateDashboardMastery(String gameName, String tagLine) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)
                ));

        PlayerStats stats = playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())
                ));

        LocalDate today = LocalDate.now();

        String content = aiRecommendationRepository.findTopByRiotUserAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                        riotUser,
                        RecommendationType.DASHBOARD_MASTERY,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                ).map(AiRecommendation::getContent)
                .orElseGet(() -> {
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s의 숙련도가 가장 높은 챔피언 '%s'을 언급하고 마무리로 친절한 존댓말로 칭찬/긍정적 멘트를 덧붙여주세요" +
                            "'챔피언 이름 + 짧은 캐치프레이즈!'로 시작하고, 숙련도 레벨과 경험치를 수치로 언급해주세요.(50자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getHighestMasteryChampion()
                    );
                    String generated = springAiOpenAiClient.sendMessage(prompt);
                    aiRecommendationRepository.save(AiRecommendation.builder()
                            .riotUser(riotUser)
                            .type(RecommendationType.DASHBOARD_MASTERY)
                            .content(generated)
                            .createdAt(LocalDateTime.now())
                            .build());
                    return generated;
                });
        return content;
    }

    public AiRecommendationResponse generateDashboardMasteryResponse(String gameName, String tagLine) {
        return AiRecommendationResponse.of(generateDashboardMastery(gameName, tagLine));
    }

    public String generateDashboardStreak(String gameName, String tagLine) {
        RiotUser riotUser = riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)
                ));
        PlayerStats stats = playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())
                ));

        LocalDate today = LocalDate.now();

        String content = aiRecommendationRepository.findTopByRiotUserAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                        riotUser,
                        RecommendationType.DASHBOARD_STREAK,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                ).map(AiRecommendation::getContent)
                .orElseGet(() -> {
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s의 현재 연승/연패 상태를 한 줄로 설명해주세요." +
                            "최근 20판 데이터만 기준으로, 20연승/20연패 경우 '최근 모든 경기'라는 표현을 사용해주세요.(50자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine()
                    );
                    String generated = springAiOpenAiClient.sendMessage(prompt);
                    aiRecommendationRepository.save(AiRecommendation.builder()
                            .riotUser(riotUser)
                            .type(RecommendationType.DASHBOARD_STREAK)
                            .content(generated)
                            .createdAt(LocalDateTime.now())
                            .build());
                    return generated;
                });
        return content;
    }

    public AiRecommendationResponse generateDashboardStreakResponse(String gameName, String tagLine) {
        return AiRecommendationResponse.of(generateDashboardStreak(gameName, tagLine));
    }


}

