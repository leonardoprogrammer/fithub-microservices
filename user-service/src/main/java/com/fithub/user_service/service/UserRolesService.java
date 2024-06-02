package com.fithub.user_service.service;

import com.fithub.user_service.model.entity.UserRoles;
import com.fithub.user_service.repository.RoleRepository;
import com.fithub.user_service.repository.UserRepository;
import com.fithub.user_service.repository.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserRolesService {

    private final UserRolesRepository userRolesRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserRolesService(UserRolesRepository userRolesRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.userRolesRepository = userRolesRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public UserRoles save(UUID userId, Long roleId) {
        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Role not found"));

        UserRoles userRoles = new UserRoles();
        userRoles.setUserId(userId);
        userRoles.setRoleId(roleId.intValue());

        return userRolesRepository.save(userRoles);
    }
}
