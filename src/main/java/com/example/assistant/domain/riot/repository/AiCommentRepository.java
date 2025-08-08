package com.example.assistant.domain.riot.repository;

import com.example.assistant.domain.riot.entity.AiComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiCommentRepository extends JpaRepository<AiComment, Long> {
}
