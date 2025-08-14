package com.example.assistant.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityMessageRequest {

    @NotBlank(message = "채팅방이 선택되지 않았습니다.")
    private String room;

    @NotBlank(message = "메세지를 입력해야 합니다.")
    @Size(max = 255, message = "255자 이하로 입력해야 합니다.")
    private String content;
}
