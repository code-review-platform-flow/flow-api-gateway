package org.flow.gateway.repository;


import java.util.Optional;
import org.flow.gateway.entity.UserSessionsEntity;
import org.flow.gateway.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSessionsRepository extends JpaRepository<UserSessionsEntity, Long> {

    @Query("SELECT us FROM UserSessionsEntity us WHERE us.user.userId = :userId")
    Optional<UserSessionsEntity> findByUserId(@Param("userId") Long userId);

}
