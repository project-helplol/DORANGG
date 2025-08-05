package com.example.assistant.domain.riot.dto.response;


import com.example.assistant.domain.riot.entity.RiotUser;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RiotUserResponse {
    private Long id;
    private String gameName;
    private String tagLine;
    private String puuid;
    private LocalDateTime createdAt;

    public static RiotUserResponse from (RiotUser user){
        // 빌더 활용
        return RiotUserResponse.builder()
                .id(user.getId())
                .gameName(user.getGameName())
                .tagLine(user.getTagLine())
                .puuid(user.getPuuid())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
