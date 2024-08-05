package org.flow.gateway.repository;

import org.flow.gateway.entity.UsersEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsersRepository extends ReactiveCrudRepository<UsersEntity, Long> {
    Mono<UsersEntity> findByEmail(String email);
}
