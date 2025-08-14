package com.example.assistant.common.websocket.interceptor;

import com.example.assistant.common.security.filter.JwtFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Key;
import java.util.List;

public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtFilter jwtFilter;

    public StompAuthChannelInterceptor(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && "CONNECT".equals(accessor.getCommand().name())) {
            List<String> auth = accessor.getNativeHeader("Authorization");
            if (auth != null && !auth.isEmpty()) {
                try {
                    String token = auth.get(0).substring(7);
                    Key key = Keys.hmacShaKeyFor(jwtFilter.getSecretKey().getBytes());
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    Long userId = Long.parseLong(claims.getSubject());
                    System.out.println("[Interceptor] 파싱된 userId: " + userId);

                    UsernamePasswordAuthenticationToken principal =
                            new UsernamePasswordAuthenticationToken(userId, null, null);
                    accessor.setUser(principal);
                    SecurityContextHolder.getContext().setAuthentication(principal);

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("STOMP 연결용 JWT 토큰이 유효하지 않습니다.");
                }
            }
        }
        return message;
    }
}