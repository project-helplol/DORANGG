package com.example.assistant.domain.riot.controller;


import com.example.assistant.domain.riot.dto.request.RiotUserRequest;
import com.example.assistant.domain.riot.dto.response.RiotUserResponse;
import com.example.assistant.domain.riot.service.RiotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/riot-user")
@RequiredArgsConstructor
public class RiotUserController {

    private final RiotUserService riotUserService;

    @PostMapping
    public ResponseEntity<RiotUserResponse> register(@RequestBody RiotUserRequest request) {
        return ResponseEntity.ok(riotUserService.register(request));
    }

    @GetMapping("/{puuid}")
    public ResponseEntity<RiotUserResponse> getByPuuid(@PathVariable String puuid) {
        return ResponseEntity.ok(riotUserService.getByPuuid(puuid));
    }

}



