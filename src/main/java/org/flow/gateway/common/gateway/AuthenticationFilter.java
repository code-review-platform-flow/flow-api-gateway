package org.flow.gateway.common.gateway;

import org.flow.gateway.service.usersessions.UserSessionsLogicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	private final UserSessionsLogicService userSessionsLogicService;

	public AuthenticationFilter(UserSessionsLogicService userSessionsLogicService) {
		super(Config.class);
		this.userSessionsLogicService = userSessionsLogicService;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			String token = extractToken(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

			if (token == null) {
				return handleUnauthorized(exchange);
			}

			return userSessionsLogicService.isTokenValidMono(token)
				.flatMap(isValid -> {
					if (isValid) {
						return userSessionsLogicService.getSession(token).flatMap(session -> {
							exchange.getRequest().mutate()
								.header(HttpHeaders.AUTHORIZATION, "Bearer " + session.getAccessToken())
								.build();
							return chain.filter(exchange);
						});
					} else {
						return handleUnauthorized(exchange);
					}
				});
		};
	}

	private String extractToken(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
		logger.warn("Unauthorized request to {}", exchange.getRequest().getPath());
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return exchange.getResponse().setComplete();
	}

	public static class Config {
		// Configuration properties if needed
	}
}