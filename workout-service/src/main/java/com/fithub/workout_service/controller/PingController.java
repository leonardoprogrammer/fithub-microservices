package com.fithub.workout_service.controller;

import com.fithub.workout_service.component.StartupTimeLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/ping")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PingController {

    @Autowired
    private Environment environment;

    private final StartupTimeLogger startupTimeLogger;

    public PingController(StartupTimeLogger startupTimeLogger) {
        this.startupTimeLogger = startupTimeLogger;
    }

    @GetMapping
    public ResponseEntity<Object> ping() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("module", environment.getProperty("service.module"));
        response.put("service", environment.getProperty("spring.application.name"));
        response.put("version", getVersion());
        response.put("startupTime", startupTimeLogger.getStartupTime());
        response.put("database", environment.getProperty("service.database"));
        response.put("databaseStatus", checkDatabaseStatus());

        return ResponseEntity.ok(response);
    }

    private String getVersion() {
        try {
            // Carregar o arquivo pom.xml
            File pomFile = new File("pom.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(pomFile);

            // Obter a versão do projeto do arquivo pom.xml
            return document.getElementsByTagName("version").item(1).getTextContent();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return "Versão não encontrada";
        }
    }

    private String checkDatabaseStatus() {
        String jdbcUrl = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.close();
            return "Database Online";
        } catch (SQLException e) {
            // Se ocorrer uma exceção ao tentar conectar, o banco de dados está inacessível
            return "Database Offline";
        }
    }
}
