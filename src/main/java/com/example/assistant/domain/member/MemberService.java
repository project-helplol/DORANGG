package com.example.assistant.domain.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

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
}
