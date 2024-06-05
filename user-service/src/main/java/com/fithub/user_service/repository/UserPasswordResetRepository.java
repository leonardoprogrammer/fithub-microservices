package com.fithub.user_service.repository;

import com.fithub.user_service.model.entity.UserPasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserPasswordResetRepository extends JpaRepository<UserPasswordReset, UUID> {
}
