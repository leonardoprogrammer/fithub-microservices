package com.fithub.user_service.service;

import com.fithub.user_service.model.entity.UserPasswordReset;
import com.fithub.user_service.repository.UserPasswordResetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserPasswordResetService {

    final UserPasswordResetRepository userPasswordResetRepository;

    @Autowired
    public UserPasswordResetService(UserPasswordResetRepository userPasswordResetRepository) {
        this.userPasswordResetRepository = userPasswordResetRepository;
    }

    @Transactional
    public UserPasswordReset save(UserPasswordReset userPasswordReset) {
        return userPasswordResetRepository.save(userPasswordReset);
    }

    @Transactional
    public void update(UserPasswordReset userPasswordReset) {
        userPasswordResetRepository.save(userPasswordReset);
    }

    public UserPasswordReset findById(UUID id) {
        return userPasswordResetRepository.findById(id).orElse(null);
    }

    public boolean existsById(UUID id) {
        return userPasswordResetRepository.existsById(id);
    }
}
