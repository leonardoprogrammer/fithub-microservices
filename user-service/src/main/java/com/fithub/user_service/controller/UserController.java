package com.fithub.user_service.controller;

import com.fithub.user_service.enums.Roles;
import com.fithub.user_service.enums.Status;
import com.fithub.user_service.model.entity.User;
import com.fithub.user_service.model.entity.UserLogin;
import com.fithub.user_service.model.entity.UserRoles;
import com.fithub.user_service.model.record.RegisterUserRequest;
import com.fithub.user_service.model.record.RequiredPasswordRequest;
import com.fithub.user_service.model.record.UpdatePasswordRequest;
import com.fithub.user_service.model.record.UpdateUserRequest;
import com.fithub.user_service.service.UserLoginService;
import com.fithub.user_service.service.UserRolesService;
import com.fithub.user_service.service.UserService;
import com.fithub.user_service.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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
    public ResponseEntity<Object> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        // VALIDATIONS
        if (Utils.isNullOrEmpty(registerUserRequest.name())) {
            return ResponseEntity.badRequest().body("Informe o nome.");
        } else if (registerUserRequest.name().length() < 3) {
            return ResponseEntity.badRequest().body("Nome deve conter o mínimo de 3 caracteres.");
        }

        if (registerUserRequest.dateBirth() == null) {
            return ResponseEntity.badRequest().body("Informe a data de nascimento.");
        } else if (registerUserRequest.dateBirth().after(new Date())) {
            return ResponseEntity.badRequest().body("Informe uma data válida.");
        }

        if (registerUserRequest.login() == null) {
            return ResponseEntity.badRequest().body("Informe as credenciais de login.");
        }

        if (Utils.isNullOrEmpty(registerUserRequest.login().email())) {
            return ResponseEntity.badRequest().body("Informe o e-mail do login.");
        }

        String email = registerUserRequest.login().email().trim();

        if (!email.contains("@") ||
                email.split("@").length != 2 ||
                !email.split("@")[1].contains(".")) {
            return ResponseEntity.badRequest().body("Informe um e-mail válido.");
        }

        if (Utils.isNullOrEmpty(registerUserRequest.login().password())) {
            return ResponseEntity.badRequest().body("Informe a senha do login.");
        }

        String password = registerUserRequest.login().password().trim();

        if (password.length() < 8) {
            return ResponseEntity.badRequest().body("Senha deve conter o mínimo de 8 caracteres.");
        }

        // CHECKS
        if (userLoginService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Já existe um login com este e-mail.");
        }

        //PROCESS
        String cryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        BeanUtils.copyProperties(registerUserRequest, user);
        user.setStatus(Status.ACTIVATED.getValue());

        User newUser = userService.save(user);

        UserLogin userLogin = new UserLogin(
                user.getId(),
                email,
                cryptedPassword
        );
        userLoginService.update(userLogin);

        UserRoles roles = userRolesService.save(newUser.getId(), Roles.BASIC.getRoleId());

        return new ResponseEntity<>(userService.findById(newUser.getId()), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest updateUserRequest) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        if (Utils.isNullOrEmpty(updateUserRequest.name())) {
            return ResponseEntity.badRequest().body("Nome não pode ficar em branco.");
        } else if (updateUserRequest.name().length() < 3) {
            return ResponseEntity.badRequest().body("Nome deve conter o mínimo de 3 caracteres.");
        }

        if (updateUserRequest.dateBirth() == null) {
            return ResponseEntity.badRequest().body("Data de nascimento não pode ficar em branco.");
        } else if (updateUserRequest.dateBirth().after(new Date())) {
            return ResponseEntity.badRequest().body("Informe uma data válida.");
        }
        // CHECKS
        if (!userService.existsById(id)) {
            return ResponseEntity.badRequest().body("Não há usuário com este ID.");
        }

        // PROCESS
        User user = userService.findById(id);
        BeanUtils.copyProperties(updateUserRequest, user);
        user.setDateAltDefault();
        userService.update(user);

        return ResponseEntity.ok("As alterações foram salvas.");
    }

    @PutMapping("/update/{id}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable UUID id, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        if (Utils.isNullOrEmpty(updatePasswordRequest.requiredPassword())) {
            return ResponseEntity.badRequest().body("Informe a senha atual.");
        }

        if (Utils.isNullOrEmpty(updatePasswordRequest.newPassword())) {
            return ResponseEntity.badRequest().body("Informe a nova senha.");
        }

        String newPassword = updatePasswordRequest.newPassword().trim();

        if (newPassword.length() < 8) {
            return ResponseEntity.badRequest().body("A nova senha deve conter o mínimo de 8 caracteres.");
        }

        UserLogin userLogin = userLoginService.findByUserId(id);

        // CHECKS
        if (userLogin == null) {
            return ResponseEntity.badRequest().body("Não há login com este ID de usuário.");
        }

        if (!BCrypt.checkpw(updatePasswordRequest.requiredPassword(), userLogin.getPassword())) {
            return ResponseEntity.badRequest().body("A senha atual está incorreta.");
        }

        // PROCESS
        String cryptedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        userLogin.setPassword(cryptedPassword);
        userLogin.setDateAltDefault();

        userLoginService.update(userLogin);

        return ResponseEntity.ok("A senha foi alterada.");
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<Object> activateUser(@PathVariable UUID id, @RequestBody RequiredPasswordRequest requiredPasswordRequest) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        if (Utils.isNullOrEmpty(requiredPasswordRequest.requiredPassword())) {
            return ResponseEntity.badRequest().body("É necessário informar a senha para ativar a conta.");
        }

        User user = userService.findById(id);

        // CHECKS
        if (user == null) {
            return ResponseEntity.badRequest().body("Não há usuário com este ID.");
        }

        UserLogin userLogin = userLoginService.findByUserId(id);

        if (!BCrypt.checkpw(requiredPasswordRequest.requiredPassword(), userLogin.getPassword())) {
            return ResponseEntity.badRequest().body("A senha informada está incorreta.");
        }

        if (user.getStatus().equals("A")) {
            return ResponseEntity.badRequest().body("Este usuário já está com a conta ativa.");
        }

        // PROCESS

        user.setStatus(Status.ACTIVATED.getValue());
        userService.update(user);

        return ResponseEntity.ok("A conta foi ativada.");
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Object> deactivateUser(@PathVariable UUID id, @RequestBody RequiredPasswordRequest requiredPasswordRequest) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        if (Utils.isNullOrEmpty(requiredPasswordRequest.requiredPassword())) {
            return ResponseEntity.badRequest().body("É necessário informar a senha para desativar a conta.");
        }

        User user = userService.findById(id);

        // CHECKS
        if (user == null) {
            return ResponseEntity.badRequest().body("Não há usuário com este ID.");
        }

        UserLogin userLogin = userLoginService.findByUserId(id);

        if (!BCrypt.checkpw(requiredPasswordRequest.requiredPassword(), userLogin.getPassword())) {
            return ResponseEntity.badRequest().body("A senha informada está incorreta.");
        }

        if (user.getStatus().equals("D")) {
            return ResponseEntity.badRequest().body("Este usuário já está com a conta desativada.");
        }

        // PROCESS
        user.setStatus(Status.DEACTIVATED.getValue());
        userService.update(user);

        return ResponseEntity.ok("A conta foi desativada.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable UUID id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>("Não há usuário com este ID.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(user);
    }
}
