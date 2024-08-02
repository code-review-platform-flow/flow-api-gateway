package org.flow.gateway.repository;


import java.util.Optional;
import org.flow.gateway.entity.UserSessionsEntity;
import org.flow.gateway.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface UserSessionsRepository extends JpaRepository<UserSessionsEntity, Long> {

    @Query("SELECT us FROM UserSessionsEntity us WHERE us.user.userId = :userId")
    Optional<UserSessionsEntity> findByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(us) > 0 THEN TRUE ELSE FALSE END FROM UserSessionsEntity us WHERE us.user.userId = :userId")
    Boolean existsByUserId(@Param("userId") Long userId);

}
