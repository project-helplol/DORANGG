package com.example.assistant.domain.member.service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.assistant.domain.member.domain.Member;
import com.example.assistant.domain.member.dto.MemberResponse;
import com.example.assistant.domain.member.repository.MemberRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${jwt.secret-key}")
	String secretKey;

	/**
	 * 회원 가입
	 * - 이미 회원 가입된 이메일인 경우?
	 * - 비밀번호는 암호화 되어야 할 것 같은데
	 */
	public void signup(String email, String password, String name, String nickname) {
		// 이메일 중복검사
		boolean isExist = memberRepository.existsByEmail(email);
		if (isExist) {
			throw new RuntimeException("이미 존재 하는 이메일입니다.");
		}

		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(password);
		Member member = new Member(email, encodedPassword, name, nickname);

		// DB 저장
		memberRepository.save(member);
	}

	public String singin(String email, String password) {
		// 우리 시스템에 회원가입한 사용자인지
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

		// 회원가입할때 입력한 비밀번호와, 로그인할때 입력한 비밀번호가 같은지 검증
		boolean isMatched = passwordEncoder.matches(password, member.getPassword());
		if (!isMatched) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}

		Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

		// TODO 입장권 : Session/JWT
		String token = Jwts.builder()
			.subject(String.valueOf(member.getId()))
			.issuedAt(new Date())
			.signWith(key, SignatureAlgorithm.HS256) // 키와 알고리즘으로 서명
			.compact();

		return token;
	}

	public MemberResponse findById(Long loginUserId) {
		Member member = memberRepository.findById(loginUserId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
		
		MemberResponse memberResponse = new MemberResponse(
			member.getId(),
			member.getEmail(),
			member.getName(),
			member.getNickname(),
			member.getCreatedAt()
		);
		return memberResponse;
	}
}
