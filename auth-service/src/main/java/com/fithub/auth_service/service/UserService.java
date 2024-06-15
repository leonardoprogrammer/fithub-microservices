package com.fithub.auth_service.service;

import com.fithub.auth_service.model.entity.User;
import com.fithub.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }
}
