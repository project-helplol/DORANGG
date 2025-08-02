package com.example.assistant.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Entity:Member")
class MemberTest {
	@Test
	@DisplayName("Member Entity 객체 생성")
	void create() {
		// When
		Member member = new Member("hong-gd@gmail.com", "password", "홍길동", "동에번쩍 서에번쩍");

		// Then
		System.out.println(member.getId());
		System.out.println(member.getEmail());
		System.out.println(member.getPassword());
		System.out.println(member.getName());
		System.out.println(member.getNickname());
	}

}
