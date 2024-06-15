package com.fithub.user_service.controller;

import com.fithub.user_service.model.entity.UserLogin;
import com.fithub.user_service.model.entity.UserPasswordReset;
import com.fithub.user_service.model.record.RegisterPasswordResetRequest;
import com.fithub.user_service.model.record.UpdatePasswordResetRequest;
import com.fithub.user_service.service.EmailService;
import com.fithub.user_service.service.UserLoginService;
import com.fithub.user_service.service.UserPasswordResetService;
import com.fithub.user_service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/password-reset")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PasswordResetController {

    final UserPasswordResetService userPasswordResetService;
    final UserLoginService userLoginService;

    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    @Autowired
    public PasswordResetController(UserPasswordResetService userPasswordResetService, UserLoginService userLoginService) {
        this.userPasswordResetService = userPasswordResetService;
        this.userLoginService = userLoginService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUserPasswordReset(@RequestBody RegisterPasswordResetRequest registerPasswordResetRequest) {
        if (Utils.isNullOrEmpty(registerPasswordResetRequest.email())) {
            return ResponseEntity.badRequest().body("Informe o e-mail.");
        }

        UserLogin userLogin = userLoginService.findByEmail(registerPasswordResetRequest.email());

        if (userLogin == null) {
            return ResponseEntity.badRequest().body("Nenhum usuário cadastrado com este e-mail.");
        }

        // REGISTRA SOLICITAÇÃO DE REDEFINIÇÃO

        UserPasswordReset userPasswordReset = new UserPasswordReset();
        userPasswordReset.setEmail(registerPasswordResetRequest.email());
        userPasswordReset.setUserLoginId(userLogin.getId());
        userPasswordReset.setDateExpiration(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));

        UserPasswordReset savedRequest = userPasswordResetService.save(userPasswordReset);

        // ENVIA E-MAIL

        String link = environment.getProperty("domain.url") + "/password-reset/" + savedRequest.getId();

        emailService.sendSimpleMessage(
                registerPasswordResetRequest.email(),
                "Redefinição de senha",
                "Clique no link para redefinir sua senha: " + link
        );

        savedRequest.setSendDate(Timestamp.valueOf(LocalDateTime.now()));

        userPasswordResetService.update(savedRequest);

        return ResponseEntity.ok("Solicitação de redefinição de senha registrada com sucesso.\n" +
                "Link para redefinição de senha enviado para o e-mail cadastrado.");
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUserPasswordReset(@RequestBody UpdatePasswordResetRequest updatePasswordResetRequest) {
        if (updatePasswordResetRequest.id() == null || Utils.isNullOrEmpty(updatePasswordResetRequest.id().toString())) {
            return ResponseEntity.badRequest().body("Informe o ID da solicitação.");
        } else if (Utils.isNullOrEmpty(updatePasswordResetRequest.password())) {
            return ResponseEntity.badRequest().body("Informe a nova senha.");
        }

        UserPasswordReset userPasswordReset = userPasswordResetService.findById(updatePasswordResetRequest.id());

        if (userPasswordReset == null) {
            return ResponseEntity.badRequest().body("O ID informado não corresponde a alguma solicitação de redefinição de senha.");
        }

        UserLogin userLogin = userLoginService.findById(userPasswordReset.getUserLoginId());

        userLogin.setPassword(BCrypt.hashpw(updatePasswordResetRequest.password(), BCrypt.gensalt()));
        userLoginService.update(userLogin);

        userPasswordReset.setReset(true);
        userPasswordReset.setResetDate(Timestamp.valueOf(LocalDateTime.now()));
        userPasswordResetService.update(userPasswordReset);

        // ENVIA E-MAIL DE CONFIRMAÇÃO DE REDEFINIÇÃO DE SENHA

        emailService.sendSimpleMessage(
                userPasswordReset.getEmail(),
                "Confirmação de redefinição de senha",
                "Sua senha foi redefinida com sucesso."
        );

        return ResponseEntity.ok("Senha redefinida com sucesso.\n" +
                "E-mail de confirmação enviado para o e-mail cadastrado.");
    }
}
