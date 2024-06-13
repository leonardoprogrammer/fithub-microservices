package com.fithub.auth_service.controller;

import com.fithub.auth_service.component.JwtTokenProvider;
import com.fithub.auth_service.model.dto.LoginResponseDTO;
import com.fithub.auth_service.model.entity.User;
import com.fithub.auth_service.model.entity.UserLogin;
import com.fithub.auth_service.model.record.LoginRequest;
import com.fithub.auth_service.service.UserLoginService;
import com.fithub.auth_service.service.UserService;
import com.fithub.auth_service.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    private final UserLoginService userLoginService;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    public AuthenticationController(UserLoginService userLoginService, UserService userService, JwtTokenProvider tokenProvider) {
        this.userLoginService = userLoginService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        Optional<UserLogin> userLoginOptional;

        if (Utils.isNullOrEmpty(loginRequest.email())) {
            if (Utils.isNullOrEmpty(loginRequest.password())) {
                return ResponseEntity.badRequest().body("Informe as credenciais.");
            }
            return ResponseEntity.badRequest().body("Informe o email.");
        } else if (Utils.isNullOrEmpty(loginRequest.password())) {
            return ResponseEntity.badRequest().body("Informe a senha.");
        }

        // Busca o usuário pelo email
        userLoginOptional = userLoginService.findByEmail(loginRequest.email());

        if (userLoginOptional.isEmpty() || !BCrypt.checkpw(loginRequest.password(), userLoginOptional.get().getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        User user = userService.findById(userLoginOptional.get().getUserId()).get();

        LoginResponseDTO response = tokenProvider.generateToken(user);

        return ResponseEntity.ok(response);
    }
}
