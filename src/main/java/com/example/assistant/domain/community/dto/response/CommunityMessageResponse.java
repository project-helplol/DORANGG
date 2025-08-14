package com.example.assistant.domain.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommunityMessageResponse {
    private Long id;
    private Long memberId;
    private String nickname;
    private String room;
    private String content;
    private LocalDateTime createdAt;
}
