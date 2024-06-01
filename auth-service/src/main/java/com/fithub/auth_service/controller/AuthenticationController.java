package com.fithub.auth_service.controller;

import com.fithub.auth_service.model.dto.RequestLoginDTO;
import com.fithub.auth_service.model.dto.ResponseLoginDTO;
import com.fithub.auth_service.model.entity.User;
import com.fithub.auth_service.model.entity.UserLogin;
import com.fithub.auth_service.service.BCryptService;
import com.fithub.auth_service.service.JwtService;
import com.fithub.auth_service.service.UserLoginService;
import com.fithub.auth_service.service.UserService;
import com.fithub.auth_service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    private final UserLoginService userLoginService;
    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(UserLoginService userLoginService, UserService userService, JwtService jwtService) {
        this.userLoginService = userLoginService;
        this.userService = userService;
        this.jwtService = jwtService;
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
            //return new ResponseEntity<>("Credenciais inválidas.", HttpStatus.NOT_FOUND);
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        User user = userService.findById(userLoginOptional.get().getUserId()).get();

        // Gera o token
        ResponseLoginDTO response = jwtService.generateToken(user.getId(), user.getRoles());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('SCOPE_BASIC')")
    public ResponseEntity<Object> test() {
        return ResponseEntity.ok("Teste de autenticação");
    }
}
