package com.example.assistant.domain.todo.controller;

import com.example.assistant.domain.todo.dto.request.CreateTodoRequest;
import com.example.assistant.domain.todo.dto.request.UpdateTodoStatusRequest;
import com.example.assistant.domain.todo.dto.response.TodoResponse;
import com.example.assistant.domain.todo.enums.TodoStatus;
import com.example.assistant.domain.todo.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")

public class TodoController {
    private final TodoService todoService;
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
            @RequestBody CreateTodoRequest request,
            @AuthenticationPrincipal Long memberId){
        return ResponseEntity.ok(todoService.createTodo(memberId, request));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<TodoResponse> getTodo(
//            @AuthenticationPrincipal Long memberId,
//            @PathVariable Long id){
//        System.out.println("멤버 값 : " + memberId);
//        return ResponseEntity.ok(todoService.getTodo(memberId,id));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTodo(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long id) {
        TodoResponse response = todoService.getTodo(memberId, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodoList(
            @AuthenticationPrincipal Long memberId,
            @RequestParam(required = false)TodoStatus status){
        return ResponseEntity.ok(todoService.getTodoList(memberId, status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TodoResponse> updateStatus(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long id,
            @RequestBody UpdateTodoStatusRequest request) {
        return ResponseEntity.ok(todoService.updateStatus(id, request, memberId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            @AuthenticationPrincipal Long memberId){
        todoService.deleteTodo(id, memberId);
        return ResponseEntity.ok().build();
    }
}