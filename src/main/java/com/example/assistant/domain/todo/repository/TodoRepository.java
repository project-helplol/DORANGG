package com.example.assistant.domain.todo.repository;

import com.example.assistant.domain.todo.entity.Todo;
import com.example.assistant.domain.todo.enums.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByMember_Id(Long memberId);
    // List<Todo> findByEndDatetimeBefore(LocalDateTime dateTime);
    List<Todo> findByMember_IdAndStatus(Long memberID, TodoStatus status);
    Optional<Todo> findByIdAndMember_Id(Long id, Long memberId);
}
