package org.flow.gateway.service.usersessions;

import java.time.LocalDateTime;

import org.flow.gateway.dto.usersessions.UserSessionsDto;
import org.flow.gateway.mapper.UserSessionsMapper;
import org.flow.gateway.service.usersessions.persistence.UserSessionsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserSessionsLogicService {
	private final UserSessionsService userSessionsService;
	private final UserSessionsMapper userSessionsMapper;

	public Mono<UserSessionsDto> getSession(String accessToken) {
		return userSessionsService.getSessionByAccessToken(accessToken)
			.map(userSessionsMapper::toDto);
	}

	public boolean isTokenValid(UserSessionsDto session) {
		return session.getExpiration().isAfter(LocalDateTime.now());
	}

	public Mono<Boolean> isTokenValidMono(String accessToken) {
		return getSession(accessToken)
			.map(this::isTokenValid)
			.defaultIfEmpty(false);
	}

}