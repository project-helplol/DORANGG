package com.example.assistant.domain.community.repository;

import com.example.assistant.domain.community.entity.CommunityMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityMessageRepository extends JpaRepository<CommunityMessage, Long> {
    List<CommunityMessage> findTop200ByRoomOrderByCreatedAtDesc(String room);
}
