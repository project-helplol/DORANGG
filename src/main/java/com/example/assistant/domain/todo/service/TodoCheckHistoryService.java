package com.example.assistant.domain.todo.service;

import com.example.assistant.domain.todo.repository.TodoCheckHistoryRepository;
import com.example.assistant.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TodoCheckHistoryService {
    private final TodoCheckHistoryRepository todoCheckHistoryRepository;
    private final TodoRepository todoRepository;
    private final RestTemplate restTemplate = new RestTemplate();
}
