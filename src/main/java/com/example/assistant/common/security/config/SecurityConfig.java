package com.example.assistant.common.security.config;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import com.example.assistant.common.security.filter.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
                .cors(Customizer.withDefaults()) // 프론트 테스트
                .csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("=== 인증 실패 ===");
                            System.out.println("Request URI: " + request.getRequestURI());
                            System.out.println("Message: " + authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("=== 접근 거부 ===");
                            System.out.println("Request URI: " + request.getRequestURI());
                            System.out.println("Message: " + accessDeniedException.getMessage());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                        })
                )
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/members/signup").permitAll()
						.requestMatchers("/api/members/signin").permitAll()
                        .requestMatchers("/api/members/my-page").authenticated()
						.requestMatchers("/api/riot").permitAll()
						.requestMatchers("/api/riot-user").permitAll()
                        .requestMatchers("/api/match").permitAll()
                        .requestMatchers("/api/match/**").permitAll()
                        .requestMatchers("/api/ai/").permitAll()
                        .requestMatchers("/api/ai/daily-briefing").permitAll()
                        .requestMatchers("/api/player-stats").permitAll()


                        .requestMatchers(HttpMethod.GET, "/api/post/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/post/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/post/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/post/**").authenticated()

                        .requestMatchers("/api/community/**").authenticated()

                        .requestMatchers("/api/todo/**").authenticated()

                        .requestMatchers("/ws-chat/**").permitAll() //.authenticated()
						.anyRequest().authenticated()
				)
			.build();
	}
    // 프론트 테스트

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
