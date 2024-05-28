package com.fithub.auth_service.repository;

import com.fithub.auth_service.model.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, UUID> {

    Optional<UserLogin> findByEmail(String email);

    boolean existsByEmail(String email);
}
