package com.example.assistant.study.jwt;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Disabled
@DisplayName("study:jwt")
public class JwtProviderTest {

	@Test
	@DisplayName("jjwt를 이용한 jwt 생성")
	void createJwt() {
		// Given
		Long userId = 1L;
		Key key = Keys.hmacShaKeyFor("어떤 발급처만 보관하고 있는 정보".getBytes());

		// When
		String token = Jwts.builder()
			.subject(userId.toString())
			.issuedAt(new Date())
			.signWith(key)
			.compact();

		// Then
		System.out.println(token);
	}

	@Test
	@DisplayName("jjwt를 이용해 생성된 jwt 해석")
	void resolverJwt() {
		// Given
		Long userId = 999L;
		Key key = Keys.hmacShaKeyFor("어떤 발급처만 보관하고 있는 정보".getBytes());

		// When
		String token = Jwts.builder()
			.subject(userId.toString())
			.issuedAt(new Date())
			.signWith(key)
			.compact();

		// When
		Key anotherKey = Keys.hmacShaKeyFor("어떤 올바르지 못한 Key 정보".getBytes());

		// Then
		Claims payload = Jwts.parser()
			.setSigningKey(anotherKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
		System.out.println(payload.getSubject());
	}
}
