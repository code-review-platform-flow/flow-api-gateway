package org.flow.gateway.repository;


import java.util.Optional;
import org.flow.gateway.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    @Query("SELECT u FROM UsersEntity u WHERE u.email = :email")
    Optional<UsersEntity> findByEmail(@Param("email") String email);

}
