package org.flow.gateway.repository;


import org.flow.gateway.entity.UserSessionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionsRepository extends JpaRepository<UserSessionsEntity, Long> {

}
