package com.example.assistant.domain.riot.service;

import com.example.assistant.domain.riot.dto.response.DashboardCardsResponse;
import com.example.assistant.domain.ai.client.SpringAiOpenAiClient;
import com.example.assistant.domain.riot.dto.response.AiRecommendationResponse;
import com.example.assistant.domain.riot.entity.AiRecommendation;
import com.example.assistant.domain.riot.entity.PlayerStats;
import com.example.assistant.domain.riot.entity.RiotUser;
import com.example.assistant.domain.riot.enums.RecommendationType;
import com.example.assistant.domain.riot.repository.AiRecommendationRepository;
import com.example.assistant.domain.riot.repository.PlayerStatsRepository;
import com.example.assistant.domain.riot.repository.RiotUserRepository;
import com.example.assistant.domain.riot.repository.MatchRepository;
import com.example.assistant.domain.riot.entity.Match;
import com.example.assistant.domain.riot.enums.GameResult;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private final MatchRepository matchRepository;
    private final RiotUserRepository riotUserRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final SpringAiOpenAiClient springAiOpenAiClient;
    private final AiRecommendationRepository aiRecommendationRepository;


    private RiotUser getRiotUser(String gameName, String tagLine) {
        return riotUserRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new RuntimeException(
                        String.format("라이엇 유저를 찾을 수 없습니다. gameName=%s, tagLine=%s", gameName, tagLine)));
    }

    private PlayerStats getStats(RiotUser riotUser) {
        return playerStatsRepository.findByRiotUser_Id(riotUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("플레이어 통계를 찾을 수 없습니다. riotUserId=%d", riotUser.getId())));
    }

    public String generateDailyBriefing(String gameName, String tagLine) {
        RiotUser riotUser = getRiotUser(gameName, tagLine);
        PlayerStats stats = getStats(riotUser);

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
                            .prompt(prompt)
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
        RiotUser riotUser = getRiotUser(gameName, tagLine);
        PlayerStats stats = getStats(riotUser);

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
                            "인사말 금지, 소환사 명#, 게임네임 출력 금지, 본문만 작성해주세요." +
                            "존댓말로 다양하게 작성하며, 부족한 부분은 개선점을 짚어주시고 잘한 부분은 칭찬해주세요. (100자 이내)",
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
                            .prompt(prompt)
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
        RiotUser riotUser = getRiotUser(gameName, tagLine);
        PlayerStats stats = getStats(riotUser);

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
                            "인사말 금지, 소환사 명# 금지, 게임 네임 출력 금지, 칭찬과 응원하지말고 본문만 작성해주세요." +
                            "코어템, 핵심룬, 스펠 기준 중심으로 존댓말로 랜덤하게 설명해주세요.(200자 이내)" ,
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
                            .prompt(prompt)
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


    public DashboardCardsResponse generateDashboardCardsResponse(String gameName, String tagLine) {
        String position = generateDashboardPosition(gameName, tagLine);
        String champion = generateDashboardChampions(gameName, tagLine);
        String tier     = generateDashboardTier(gameName, tagLine);
        String winRate  = generateDashboardWinRate(gameName, tagLine);
        String mastery  = generateDashboardMastery(gameName, tagLine);
        String streak   = generateDashboardStreak(gameName, tagLine);

        return DashboardCardsResponse.builder()
                .position(position)
                .champion(champion)
                .tier(tier)
                .winRate(winRate)
                .mastery(mastery)
                .streak(streak)
                .build();
    }


    public String generateDashboardPosition(String gameName, String tagLine) {
        RiotUser riotUser = getRiotUser(gameName, tagLine);
        PlayerStats stats = getStats(riotUser);

        String content = String.join("|",
                safe(stats.getFavoritePosition()),
                String.format("%.3f", stats.getWinRate()),
                String.valueOf(stats.getTotalGames()),
                String.valueOf(stats.getWinCount()),
                timeKey(stats.getUpdatedAt()));
        String hash = sha256(content);
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s의 최근 포지션별 승률을 반드시 두 줄로 작성하며 형식은 고정입니다. " +
                            "‘최근 [포지션명] 승률은 [수치]%% [상승/하락]했습니다. [▲/▼]’ " +
                            "▲는 상승, ▼는 하락일 때만 사용하며 인사말은 금지.",
                            riotUser.getGameName(),
                            riotUser.getTagLine()
                    );
        return reuseOrGenerate(riotUser, stats, RecommendationType.DASHBOARD_POSITION, prompt, hash);
    }


    public String generateDashboardChampions(String gameName, String tagLine) {
        RiotUser riotUser = getRiotUser(gameName, tagLine);
        PlayerStats stats = getStats(riotUser);

        String content = String.join("|",
                safe(stats.getMostPlayedChampion()),
                timeKey(stats.getUpdatedAt()));
        String hash = sha256(content);
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s님의 가장 자주 플레이하는 챔피언은 '%s'입니다." +
                            "인사말 금지, 소환사 명 # 출력 금지, 본문만 작성해주세요." +
                            "챔피언에 대해 항상 존댓말로 간단히 말씀해주세요.(50자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getMostPlayedChampion()
                    );

        return reuseOrGenerate(riotUser, stats, RecommendationType.DASHBOARD_CHAMPION, prompt, hash);
    }


    public String generateDashboardTier(String gameName, String tagLine) {
        RiotUser riotUser = getRiotUser(gameName, tagLine);
        PlayerStats stats = getStats(riotUser);

        String content = String.join("|",
                safe(stats.getTier()),
                timeKey(stats.getUpdatedAt()));
        String hash = sha256(content);
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s의 현재 티어 '%s' 최근 며칠간의 순위 추세(상승/정체/하락)만 요약해주세요, " +
                            "인사말 금지, 소환사 명 출력 금지, 본문만 작성해주세요." +
                            "존댓말로 작성(50자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getTier()
                    );

        return reuseOrGenerate(riotUser, stats, RecommendationType.DASHBOARD_TIER, prompt, hash);
    }

    public String generateDashboardWinRate(String gameName, String tagLine) {
        RiotUser riotUser = getRiotUser(gameName, tagLine);
        PlayerStats stats = getStats(riotUser);

        String content = String.join("|",
                String.valueOf(stats.getTotalGames()),
                String.valueOf(stats.getWinCount()),
                String.format("%.3f", stats.getWinRate()),
                timeKey(stats.getUpdatedAt()));
        String hash = sha256(content);
                    String prompt = String.format(
                            "LoL 코치로서 %s#%s 전체 전적은 %d전 %d승이며 승률은 %.1f%%입니다." +
                            "'존댓말로'라는 말을 쓰지마" +
                            "인사말 금지, #소환사 명 출력 금지, 본문만 존댓말로 작성해주세요." +
                            "20판중 몇 판 승리했는지와 패배했는지 작성해주세요.(50자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getTotalGames(),
                            stats.getWinCount(),
                            stats.getWinRate()
                    );

        return reuseOrGenerate(riotUser, stats, RecommendationType.DASHBOARD_WINRATE, prompt, hash);
    }

    public String generateDashboardMastery(String gameName, String tagLine) {
        RiotUser riotUser = getRiotUser(gameName, tagLine);
        PlayerStats stats = getStats(riotUser);

        String content = String.join("|",
                safe(stats.getHighestMasteryChampion()),
                timeKey(stats.getUpdatedAt()));
        String hash = sha256(content);

                    String prompt = String.format(
                            "LoL 코치로서 %s#%s의 숙련도가 가장 높은 챔피언 '%s'을 언급하고 마무리로 존댓말로 긍정적 멘트를 덧붙여주세요" +
                            "인사말 금지, #소환사 명 출력 금지, 본문만 존댓말로 작성해주세요." +
                            "짧은 캐치프레이즈! 시작해주세요(50자 이내)",
                            riotUser.getGameName(),
                            riotUser.getTagLine(),
                            stats.getHighestMasteryChampion()
                    );
        return reuseOrGenerate(riotUser, stats, RecommendationType.DASHBOARD_MASTERY, prompt, hash);
    }

    public String generateDashboardStreak(String gameName, String tagLine) {
        RiotUser ru = getRiotUser(gameName, tagLine);
        var list = matchRepository.findTop20ByRiotUserOrderByMatchDateTimeDesc(ru);

        int streak = 0;
        Boolean firstWin = null;

        for (Match m : list) {
            if (m.getResult() == null) continue;
            boolean win = (m.getResult() == GameResult.WIN);
            if (firstWin == null) firstWin = win;
            if (win != firstWin) break;
            if (++streak >= 20) break;
        }

        if (firstWin == null || streak == 0) return "최근 연승·연패 흐름은 없어요.";
        if (streak >= 20) return firstWin ? "최근 모든 경기에서 승리하셨어요." : "최근 모든 경기에서 아쉬운 결과였어요.";
        return firstWin ? String.format("최근 %d연승 중이에요.", streak)
                : String.format("최근 %d연패 상태예요.", streak);
    }

    private String reuseOrGenerate(
            RiotUser riotUser,
            PlayerStats stats,
            RecommendationType type,
            String prompt,
            String sourceHash
    ) {
        LocalDateTime statsUpdatedAt = stats.getUpdatedAt();

        Optional<AiRecommendation> latestOpt =
                aiRecommendationRepository.findTopByRiotUserAndTypeOrderByCreatedAtDesc(riotUser, type);

        if (latestOpt.isPresent()) {
            AiRecommendation latest = latestOpt.get();
            if (sourceHash.equals(latest.getSourceHash())
                    && safeTime(statsUpdatedAt).equals(safeTime(latest.getSourceStatsUpdatedAt()))) {
                return latest.getContent();
            }
        }

        String generated = springAiOpenAiClient.sendMessage(prompt);
        AiRecommendation rec = AiRecommendation.builder()
                .riotUser(riotUser)
                .type(type)
                .content(generated)
                .prompt(prompt)
                .sourceHash(sourceHash)
                .sourceStatsUpdatedAt(statsUpdatedAt)
                .createdAt(LocalDateTime.now())
                .build();
        aiRecommendationRepository.save(rec);
        return generated;
    }

    private static String timeKey(LocalDateTime t) { return t == null ? "null" : t.toString(); }
    private static LocalDateTime safeTime(LocalDateTime t) { return t == null ? LocalDateTime.MIN : t; }
    private static String safe(String s) { return s == null ? "" : s; }

    private static String sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(out);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 계산 실패", e);
        }
    }
    private static String bytesToHex(byte[] bytes) {
        final char[] HEX = "0123456789abcdef".toCharArray();
        char[] out = new char[bytes.length * 2];
        for (int i = 0, j = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            out[j++] = HEX[v >>> 4];
            out[j++] = HEX[v & 0x0F];
        }
        return new String(out);
    }
}

