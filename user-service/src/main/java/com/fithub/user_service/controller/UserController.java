package com.fithub.user_service.controller;

import com.fithub.user_service.enums.Roles;
import com.fithub.user_service.enums.Status;
import com.fithub.user_service.model.dto.RequestRegisterUserDTO;
import com.fithub.user_service.model.entity.User;
import com.fithub.user_service.model.entity.UserLogin;
import com.fithub.user_service.model.entity.UserRoles;
import com.fithub.user_service.service.UserLoginService;
import com.fithub.user_service.service.UserRolesService;
import com.fithub.user_service.service.UserService;
import com.fithub.user_service.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;
    private final UserLoginService userLoginService;
    private final UserRolesService userRolesService;

    @Autowired
    public UserController(UserService userService, UserLoginService userLoginService, UserRolesService userRolesService) {
        this.userService = userService;
        this.userLoginService = userLoginService;
        this.userRolesService = userRolesService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RequestRegisterUserDTO requestRegisterUserDTO) {
        // VALIDATIONS
        if (Utils.isNullOrEmpty(requestRegisterUserDTO.getName())) {
            return ResponseEntity.badRequest().body("Informe o nome.");
        } else if (requestRegisterUserDTO.getName().length() < 3) {
            return ResponseEntity.badRequest().body("Nome deve conter o mínimo de 3 caracteres.");
        }

        if (requestRegisterUserDTO.getDateBirth() == null) {
            return ResponseEntity.badRequest().body("Informe a data de nascimento.");
        } else if (requestRegisterUserDTO.getDateBirth().after(new Date())) {
            return ResponseEntity.badRequest().body("Informe uma data válida.");
        }

        if (Utils.isNullOrEmpty(requestRegisterUserDTO.getGender())) {
            return ResponseEntity.badRequest().body("Informe o gênero.");
        }

        if (requestRegisterUserDTO.getLogin() == null) {
            return ResponseEntity.badRequest().body("Informe as credenciais de login.");
        }

        if (Utils.isNullOrEmpty(requestRegisterUserDTO.getLogin().getEmail())) {
            return ResponseEntity.badRequest().body("Informe o e-mail do login.");
        } else if (!requestRegisterUserDTO.getLogin().getEmail().contains("@") ||
                requestRegisterUserDTO.getLogin().getEmail().split("@").length != 2 ||
                !requestRegisterUserDTO.getLogin().getEmail().split("@")[1].contains(".")) {
            return ResponseEntity.badRequest().body("Informe um e-mail válido.");
        }

        if (Utils.isNullOrEmpty(requestRegisterUserDTO.getLogin().getPassword())) {
            return ResponseEntity.badRequest().body("Informe a senha do login.");
        } else if (requestRegisterUserDTO.getLogin().getPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Senha deve conter o mínimo de 8 caracteres.");
        }

        // CHECKS
        if (userLoginService.existsByEmail(requestRegisterUserDTO.getLogin().getEmail())) {
            return ResponseEntity.badRequest().body("Já existe um login com este e-mail.");
        }

        String passwordCrypted = BCrypt.hashpw(requestRegisterUserDTO.getLogin().getPassword(), BCrypt.gensalt());

        User user = new User();
        BeanUtils.copyProperties(requestRegisterUserDTO, user);
        user.setStatus(Status.ACTIVE.getValue());

        User newUser = userService.save(user);
        newUser.setUserLogin(new UserLogin(user.getId(),
                requestRegisterUserDTO.getLogin().getEmail(),
                passwordCrypted));
        userService.update(newUser);

        UserRoles roles = userRolesService.save(newUser.getId(), Roles.BASIC.getRoleId());

        Optional<User> createdUser = userService.findById(newUser.getId());

        return ResponseEntity.ok(createdUser.orElse(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable UUID id) {
        User user = userService.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/login")
    public ResponseEntity<Object> getUserLoginByUserId(@PathVariable UUID id) {
        UserLogin userLogin = userLoginService.findByUserId(id).orElse(null);

        if (userLogin == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userLogin);
    }
}
