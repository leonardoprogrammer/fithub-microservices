package com.fithub.user_service.controller;

import com.fithub.user_service.enums.Roles;
import com.fithub.user_service.enums.Status;
import com.fithub.user_service.model.dto.RequestRegisterUserDTO;
import com.fithub.user_service.model.dto.RequestUpdateUserDTO;
import com.fithub.user_service.model.dto.RequestUpdatePasswordDTO;
import com.fithub.user_service.model.dto.RequestRequiredPasswordDTO;
import com.fithub.user_service.model.entity.User;
import com.fithub.user_service.model.entity.UserLogin;
import com.fithub.user_service.model.entity.UserRoles;
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

        String cryptedPassword = BCrypt.hashpw(requestRegisterUserDTO.getLogin().getPassword(), BCrypt.gensalt());

        User user = new User();
        BeanUtils.copyProperties(requestRegisterUserDTO, user);
        user.setStatus(Status.ACTIVATED.getValue());

        User newUser = userService.save(user);
        newUser.setUserLogin(new UserLogin(user.getId(),
                requestRegisterUserDTO.getLogin().getEmail(),
                cryptedPassword));
        userService.update(newUser);

        UserRoles roles = userRolesService.save(newUser.getId(), Roles.BASIC.getRoleId());

        return ResponseEntity.ok(userService.findById(newUser.getId()).orElse(null));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable UUID id, @RequestBody RequestUpdateUserDTO requestUpdateUserDTO) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        if (Utils.isNullOrEmpty(requestUpdateUserDTO.getName())) {
            return ResponseEntity.badRequest().body("Nome não pode ficar em branco.");
        } else if (requestUpdateUserDTO.getName().length() < 3) {
            return ResponseEntity.badRequest().body("Nome deve conter o mínimo de 3 caracteres.");
        }

        if (requestUpdateUserDTO.getDateBirth() == null) {
            return ResponseEntity.badRequest().body("Data de nascimento não pode ficar em branco.");
        } else if (requestUpdateUserDTO.getDateBirth().after(new Date())) {
            return ResponseEntity.badRequest().body("Informe uma data válida.");
        }

        if (Utils.isNullOrEmpty(requestUpdateUserDTO.getEmail())) {
            return ResponseEntity.badRequest().body("E-mail não pode ficar em branco.");
        } else if (!requestUpdateUserDTO.getEmail().contains("@") ||
                requestUpdateUserDTO.getEmail().split("@").length != 2 ||
                !requestUpdateUserDTO.getEmail().split("@")[1].contains(".")) {
            return ResponseEntity.badRequest().body("Informe um e-mail válido.");
        }

        if (Utils.isNullOrEmpty(requestUpdateUserDTO.getRequiredPassword())) {
            return ResponseEntity.badRequest().body("É necessário informar a senha para alterar.");
        }

        User user = userService.findById(id).orElse(null);

        // CHECKS
        if (user == null) {
            return ResponseEntity.badRequest().body("Não há usuário com este ID.");
        }

        if (userLoginService.existsByEmail(requestUpdateUserDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Já existe um login com este e-mail.");
        }

        UserLogin userLogin = userLoginService.findByUserId(id).get();

        if (!BCrypt.checkpw(requestUpdateUserDTO.getRequiredPassword(), userLogin.getPassword())) {
            return ResponseEntity.badRequest().body("A senha está incorreta.");
        }

        // PROCESS

        BeanUtils.copyProperties(requestUpdateUserDTO, user);
        user.setDateAltDefault();
        /*user.getUserLogin().setEmail(requestUpdateUserDTO.getEmail());
        user.getUserLogin().setDateAltDefault();*/
        userService.update(user);

        userLogin.setEmail(requestUpdateUserDTO.getEmail());
        userLogin.setDateAltDefault();
        userLoginService.update(userLogin);

        return ResponseEntity.ok("As alterações foram salvas.");
    }

    @PutMapping("/update/{id}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable UUID id, @RequestBody RequestUpdatePasswordDTO requestUpdatePasswordDTO) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        if (Utils.isNullOrEmpty(requestUpdatePasswordDTO.getRequiredPassword())) {
            return ResponseEntity.badRequest().body("Informe a senha atual.");
        }

        if (Utils.isNullOrEmpty(requestUpdatePasswordDTO.getNewPassword())) {
            return ResponseEntity.badRequest().body("Informe a nova senha.");
        } else if (requestUpdatePasswordDTO.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body("A nova senha deve conter o mínimo de 8 caracteres.");
        }

        UserLogin userLogin = userLoginService.findByUserId(id).orElse(null);

        // CHECKS
        if (userLogin == null) {
            return ResponseEntity.badRequest().body("Não há login com este ID de usuário.");
        }

        if (!BCrypt.checkpw(requestUpdatePasswordDTO.getRequiredPassword(), userLogin.getPassword())) {
            return ResponseEntity.badRequest().body("A senha atual está incorreta.");
        }

        // PROCESS

        String cryptedPassword = BCrypt.hashpw(requestUpdatePasswordDTO.getNewPassword(), BCrypt.gensalt());

        userLogin.setPassword(cryptedPassword);
        userLogin.setDateAltDefault();

        userLoginService.update(userLogin);

        return ResponseEntity.ok("A senha foi alterada.");
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<Object> activateUser(@PathVariable UUID id, @RequestBody RequestRequiredPasswordDTO requestRequiredPasswordDTO) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        if (Utils.isNullOrEmpty(requestRequiredPasswordDTO.getRequiredPassword())) {
            return ResponseEntity.badRequest().body("É necessário informar a senha para ativar a conta.");
        }

        User user = userService.findById(id).orElse(null);

        // CHECKS
        if (user == null) {
            return ResponseEntity.badRequest().body("Não há usuário com este ID.");
        }

        UserLogin userLogin = userLoginService.findByUserId(id).get();

        if (!BCrypt.checkpw(requestRequiredPasswordDTO.getRequiredPassword(), userLogin.getPassword())) {
            return ResponseEntity.badRequest().body("A senha está incorreta.");
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
    public ResponseEntity<Object> deactivateUser(@PathVariable UUID id, @RequestBody RequestRequiredPasswordDTO requestRequiredPasswordDTO) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        if (Utils.isNullOrEmpty(requestRequiredPasswordDTO.getRequiredPassword())) {
            return ResponseEntity.badRequest().body("É necessário informar a senha para desativar a conta.");
        }

        User user = userService.findById(id).orElse(null);

        // CHECKS
        if (user == null) {
            return ResponseEntity.badRequest().body("Não há usuário com este ID.");
        }

        UserLogin userLogin = userLoginService.findByUserId(id).get();

        if (!BCrypt.checkpw(requestRequiredPasswordDTO.getRequiredPassword(), userLogin.getPassword())) {
            return ResponseEntity.badRequest().body("A senha está incorreta.");
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

        User user = userService.findById(id).orElse(null);

        if (user == null) {
            return new ResponseEntity<>("Não há usuário com este ID.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/login")
    public ResponseEntity<Object> getUserLoginByUserId(@PathVariable UUID id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        UserLogin userLogin = userLoginService.findByUserId(id).orElse(null);

        if (userLogin == null) {
            return new ResponseEntity<>("Não há login com este ID de usuário.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(userLogin);
    }
}
