package com.example.assistant.domain.riot.controller;

import com.example.assistant.domain.riot.dto.response.RiotUserResponse;
import com.example.assistant.domain.riot.service.RiotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class RiotUserController {
    private final RiotUserService riotUserService;

    @GetMapping
    public ResponseEntity<RiotUserResponse> getUser(
            @RequestParam String riotId,
            @RequestParam String tagline
    ){
        RiotUserResponse user = riotUserService.findByRiotIdAndTagline(riotId, tagline);
        return ResponseEntity.ok(user);
    }
}
