//package com.example.assistant.domain.todo.service;
//
//import com.example.assistant.domain.member.entity.Member;
//import com.example.assistant.domain.member.repository.MemberRepository;
//import com.example.assistant.domain.todo.dto.request.CreateTodoRequest;
//import com.example.assistant.domain.todo.dto.request.UpdateTodoStatusRequest;
//import com.example.assistant.domain.todo.dto.response.TodoResponse;
//import com.example.assistant.domain.todo.entity.Todo;
//import com.example.assistant.domain.todo.enums.TodoStatus;
//import com.example.assistant.domain.todo.repository.TodoRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class TodoService {
//    private final TodoRepository todoRepository;
//    private final MemberRepository memberRepository;
//
//    public TodoResponse createTodo(Long memberId, CreateTodoRequest request) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new EntityNotFoundException("작성자를 찾을 수 없습니다."));
//
//        TodoStatus status = (request.getStatus() == null) ? TodoStatus.PENDING : request.getStatus();
//
//        Todo todo = Todo.builder()
//                .member(member)
//                .title(request.getTitle())
//                .content(request.getContent())
//                .status(status)
//                .build();
//
//        todoRepository.save(todo);
//        return toResponse(todo);
//    }
//
//    @Transactional(readOnly = true)
//    public TodoResponse getTodo(Long memberId, Long id) {
//        Todo todo = todoRepository.findByIdAndMember_Id(id, memberId)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "해당 목표를 찾을 수 없습니다. 로그인한 사용자 본인만 조회 가능"
//                ));
//        return toResponse(todo);
//    }
//
//    @Transactional(readOnly = true)
//    public List<TodoResponse> getTodoList(Long memberId, TodoStatus status) {
//        List<Todo> todos = (status == null)
//                ? todoRepository.findByMember_Id(memberId)
//                : todoRepository.findByMember_IdAndStatus(memberId, status);
//
//        return todos.stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    public TodoResponse updateStatus(Long id, UpdateTodoStatusRequest request, Long memberId) {
//        Todo todo = todoRepository.findByIdAndMember_Id(id, memberId)
//                .orElseThrow(() -> new EntityNotFoundException("목표를 수정 할 수 없습니다."));
//        todo.setStatus(request.getStatus());
//        return toResponse(todo);
//    }
//
//    public void deleteTodo(Long id, Long memberId) {
//        Todo todo = todoRepository.findByIdAndMember_Id(id, memberId)
//                .orElseThrow(() -> new EntityNotFoundException("목표를 삭제 할 수 없습니다."));
//        todoRepository.delete(todo);
//    }
//
//    private TodoResponse toResponse(Todo todo) {
//        return TodoResponse.builder()
//                .id(todo.getId())
//                .title(todo.getTitle())
//                .content(todo.getContent())
//                .status(todo.getStatus())
//                .build();
//    }
//}