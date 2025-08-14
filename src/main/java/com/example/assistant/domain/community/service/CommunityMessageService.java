package com.example.assistant.domain.community.service;

import com.example.assistant.domain.community.dto.request.CommunityMessageRequest;
import com.example.assistant.domain.community.dto.response.CommunityMessageResponse;
import com.example.assistant.domain.community.entity.CommunityMessage;
import com.example.assistant.domain.community.repository.CommunityMessageRepository;
import com.example.assistant.domain.member.domain.Member;
import com.example.assistant.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityMessageService {

    private final CommunityMessageRepository communityMessageRepository;
    private final MemberRepository memberRepository;

    @Value("${chat.rooms}")
    private String chatRoomsStr;

    private List<String> allowedRooms;

    @PostConstruct
    public void init() {
        allowedRooms = Arrays.asList(chatRoomsStr.split(","));
    }

    @Transactional(readOnly = true)
    public boolean exists(String room) {
        return allowedRooms.contains(room);
    }

    @Transactional
    public CommunityMessageResponse saveMessage(Long memberId, CommunityMessageRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        String room = request.getRoom();

        if (!allowedRooms.contains(room)) {
            throw new RuntimeException("허용되지 않은 채팅방입니다.");
        }

        CommunityMessage message = CommunityMessage.builder()
                .memberId(memberId)
                .nickname(member.getNickname())
                .room(room)
                .content(request.getContent())
                .build();

        CommunityMessage saved = communityMessageRepository.save(message);

        return new CommunityMessageResponse(
                saved.getId(),
                saved.getMemberId(),
                saved.getNickname(),
                saved.getRoom(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<CommunityMessageResponse> getLatestMessages(String room) {
        if (!allowedRooms.contains(room)) {
            throw new RuntimeException("허용되지 않은 채팅방입니다.");
        }

        return communityMessageRepository.findTop200ByRoomOrderByCreatedAtDesc(room)
                .stream()
                .map(m -> new CommunityMessageResponse(
                        m.getId(),
                        m.getMemberId(),
                        m.getNickname(),
                        m.getRoom(),
                        m.getContent(),
                        m.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}