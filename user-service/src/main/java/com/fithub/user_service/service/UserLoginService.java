package com.fithub.user_service.service;

import com.fithub.user_service.model.entity.UserLogin;
import com.fithub.user_service.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserLoginService {

    final UserLoginRepository userLoginRepository;

    @Autowired
    public UserLoginService(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    @Transactional
    public UserLogin save(UserLogin userLogin) {
        return userLoginRepository.save(userLogin);
    }

    @Transactional
    public void update(UserLogin userLogin) {
        userLoginRepository.save(userLogin);
    }

    public UserLogin findById(UUID id) {
        return userLoginRepository.findById(id).orElse(null);
    }

    public UserLogin findByUserId(UUID userId) {
        return userLoginRepository.findByUserId(userId).orElse(null);
    }

    public UserLogin findByEmail(String email) {
        return userLoginRepository.findByEmail(email).orElse(null);
    }

    public boolean existsById(UUID id) {
        return userLoginRepository.existsById(id);
    }

    public boolean existsByEmail(String email) {
        return userLoginRepository.existsByEmail(email);
    }
}
