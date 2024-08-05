package org.flow.gateway.repository;

import org.flow.gateway.entity.UserSessionsEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserSessionsRepository extends ReactiveCrudRepository<UserSessionsEntity, Long> {
    Mono<UserSessionsEntity> findByAccessToken(String accessToken);
    Mono<UserSessionsEntity> findByUserId(Long userId);
}