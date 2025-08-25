package com.example.assistant.domain.member.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.assistant.domain.member.entity.Member;
@Disabled
@DisplayName("Repository:Member")
@DataJpaTest
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("회원 저장")
	void save() {
		// Given
		Member member = new Member("hong-gd@gmail.com", "password", "홍길동", "동에번쩍 서에번쩍");

		// When
		memberRepository.save(member);

		// Then
		System.out.println(member.getId());
	}

}
