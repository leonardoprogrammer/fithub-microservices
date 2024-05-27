package com.fithub.auth_service.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StartupTimeLogger implements ApplicationRunner {

    @Autowired
    private Environment environment;

    private final LocalDateTime startupTime;

    public StartupTimeLogger() {
        this.startupTime = LocalDateTime.now();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Log da hora de inicialização da aplicação
        String serviceName = environment.getProperty("spring.application.name");
        System.out.println(serviceName + " iniciado em: " + getFormattedStartupTime());
    }

    public LocalDateTime getStartupTime() {
        return startupTime;
    }

    public String getFormattedStartupTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return startupTime.format(formatter);
    }
}
