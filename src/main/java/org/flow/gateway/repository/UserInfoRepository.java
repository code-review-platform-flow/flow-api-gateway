package org.flow.gateway.repository;

import org.flow.gateway.entity.UserInfoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface UserInfoRepository extends ReactiveCrudRepository<UserInfoEntity, Long> {
    Flux<UserInfoEntity> findAllByUserId(Long userId);
}
