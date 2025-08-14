package com.example.assistant.domain.todo.repository;

import com.example.assistant.domain.todo.entity.TodoCheckHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoCheckHistoryRepository extends JpaRepository<TodoCheckHistory, Long> {
    List<TodoCheckHistory> findByTodoId(Long todoId);
}
