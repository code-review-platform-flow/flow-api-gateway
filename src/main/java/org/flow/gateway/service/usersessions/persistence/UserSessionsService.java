package org.flow.gateway.service.usersessions.persistence;

import org.flow.gateway.entity.UserSessionsEntity;
import org.flow.gateway.repository.UserSessionsRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserSessionsService {

	private final UserSessionsRepository userSessionsRepository;

	public Mono<UserSessionsEntity> getSessionByAccessToken(String accessToken) {
		return userSessionsRepository.findByAccessToken(accessToken)
			.filter(UserSessionsEntity::isUseYn);
	}

}
