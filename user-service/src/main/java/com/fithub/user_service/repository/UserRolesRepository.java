package com.fithub.user_service.repository;

import com.fithub.user_service.model.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, UserRoles.UserRolesId> {
}
