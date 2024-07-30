package org.flow.gateway.repository;


import java.util.Optional;
import org.flow.gateway.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.useYn = true")
    Optional<UserEntity> findByEmail(@Param("email") String email);

}
