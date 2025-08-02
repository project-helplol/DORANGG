package com.example.assistant.common.security;

import static org.springframework.http.HttpHeaders.*;

import java.io.IOException;
import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	@Value("${jwt.secret-key}")
	private String secretKey;

	/**
	 * GET http://localhost:8080/api/my-page
	 * Content-type: application/json
	 * Authorization: eyJhbGciOiJ9.eyJzdWIiOiIxIiNzU0MTE2Nzg0fQ.cvam91ClQ9ztgM4Z50XAM
	 *
	 * 요청 헤더에서 입장권 추출 (Authorization)
	 * 요청 헤더에서 추출한 입장권이 우리가 발급한 입장권인지 검사
	 * 우리가 발급한 입장권이라면, Application 전체에서 인증된 사용자 정보를 사용할 수 있는 환경 구성
	 */
	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		// 1. 요청 헤더에서 입장권 추출 (Authorization)
		String token = request.getHeader(AUTHORIZATION);
		
		if (token == null) {
			filterChain.doFilter(request, response);
			return;
		}

		// 2. 입장권이 우리가 발급한 입장권인지 검사
		Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

		Claims payload = Jwts.parser()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getPayload();
		Long userId = Long.parseLong(payload.getSubject());

		// 3. Application 전체에서 인증된 사용자 정보를 사용할 수 있는 환경 구성 : Security Context Holder
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
			userId, null, null
		);
		SecurityContextHolder.getContext()
			.setAuthentication(usernamePasswordAuthenticationToken);

		filterChain.doFilter(request, response);
	}
}
