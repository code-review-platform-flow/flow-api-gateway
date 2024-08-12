package org.flow.gateway.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
			.csrf(csrf -> csrf
				.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()) // CSRF 토큰을 쿠키로 전송
				.disable() // 개발 또는 특정 경로에 대해 CSRF 보호 비활성화
			)
			.authorizeExchange(exchanges -> exchanges
				.pathMatchers("/auth/**", "/api/admin/**").permitAll() // /auth/** 경로는 인증 없이 접근 가능
				.anyExchange().authenticated() // 다른 모든 경로는 인증 필요
			)
			.formLogin(Customizer.withDefaults())
			.httpBasic(Customizer.withDefaults());

		return http.build();
	}
}
