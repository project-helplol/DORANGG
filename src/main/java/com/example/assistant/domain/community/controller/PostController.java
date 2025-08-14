package com.example.assistant.domain.community.controller;

import com.example.assistant.domain.community.dto.request.PostRequest;
import com.example.assistant.domain.community.dto.response.PostResponse;
import com.example.assistant.domain.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody PostRequest request,
            @AuthenticationPrincipal Long memberId) {

        return ResponseEntity.ok(postService.createPost(request, memberId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPostList() {
        return ResponseEntity.ok(postService.getPostList());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequest request,
            @AuthenticationPrincipal Long memberId) {

        return ResponseEntity.ok(postService.updatePost(id, request, memberId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal Long memberId) {

        postService.deletePost(id, memberId);
        return ResponseEntity.noContent().build();
    }
}