package com.fithub.auth_service.controller;

import com.fithub.auth_service.model.dto.RequestLoginDTO;
import com.fithub.auth_service.model.dto.ResponseLoginDTO;
import com.fithub.auth_service.model.entity.UserLogin;
import com.fithub.auth_service.service.BCryptService;
import com.fithub.auth_service.service.UserLoginService;
import com.fithub.auth_service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    private final UserLoginService userLoginService;

    @Autowired
    public AuthenticationController(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody RequestLoginDTO requestLoginDTO) {
        Optional<UserLogin> userLoginOptional;

        if (Utils.isNullOrEmpty(requestLoginDTO.getEmail())) {
            if (Utils.isNullOrEmpty(requestLoginDTO.getPassword())) {
                return ResponseEntity.badRequest().body("Informe as credenciais.");
            }
            return ResponseEntity.badRequest().body("Informe o email.");
        } else if (Utils.isNullOrEmpty(requestLoginDTO.getPassword())) {
            return ResponseEntity.badRequest().body("Informe a senha.");
        }

        // Busca o usuário pelo email
        userLoginOptional = userLoginService.findByEmail(requestLoginDTO.getEmail());

        if (userLoginOptional.isEmpty() || !BCryptService.checkPassword(requestLoginDTO.getPassword(), userLoginOptional.get().getPassword())) {
            return new ResponseEntity<>("Credenciais inválidas.", HttpStatus.NOT_FOUND);
        }

        // Gera o token
        var token = "token";

        return ResponseEntity.ok(new ResponseLoginDTO(token));
    }
}
