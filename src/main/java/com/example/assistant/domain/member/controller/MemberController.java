package com.example.assistant.domain.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.assistant.domain.member.dto.response.MemberResponse;
import com.example.assistant.domain.member.dto.request.SigninRequest;
import com.example.assistant.domain.member.dto.request.SignupRequest;
import com.example.assistant.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	/**
	 * 회원 가입 요청 정보를 수신하고 그 결과를 반환
	 * POST http://localhost:8080/api/members/singup
	 *
	 * Contents-type: application/json
	 *
	 * {
	 *     "email" : "hong-gd@gmail.com",
	 *     "password" : "password",
	 *     "name" : "홍길동",
	 *     "nickname" : "동에번쩍 서에번쩍"
	 * }
	 */
    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@RequestBody SignupRequest request) {
        MemberResponse response = memberService.signup(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getNickname()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

	/**
	 * POST http://localhost:8080/api/members/singin
	 *
	 * Contents-type: application/json
	 *
	 * {
	 *     "email" : "hong-gd@gmail.com",
	 *     "password" : "password"
	 * }
	 */
    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> signin(@RequestBody SigninRequest signinRequest) {
        String token = memberService.singin(signinRequest.getEmail(), signinRequest.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그인 성공!");
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

	@GetMapping("/my-page")
	ResponseEntity<MemberResponse> myPage(
		@AuthenticationPrincipal Long loginUserId
	) {
		MemberResponse memberResponse = memberService.findById(loginUserId);
		return ResponseEntity.ok().body(memberResponse);
	}

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(
            @AuthenticationPrincipal Long memberId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String tokenHeader) {

        String token = tokenHeader.substring(7);
        memberService.deleteMember(memberId, token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String tokenHeader) {

        String token = tokenHeader.substring(7);
        memberService.logout(token);
        return ResponseEntity.noContent().build();
    }
}
