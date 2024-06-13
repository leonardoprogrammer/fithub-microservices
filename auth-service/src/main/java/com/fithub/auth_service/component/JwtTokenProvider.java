package com.fithub.auth_service.component;

import com.fithub.auth_service.model.dto.LoginResponseDTO;
import com.fithub.auth_service.model.entity.Role;
import com.fithub.auth_service.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

@Component
public class JwtTokenProvider {

    @Value("${jwt.private.key}")
    private Resource privateKeyResource;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public LoginResponseDTO generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        List<String> authorities = new ArrayList<>();

        for (Role role : user.getRoles())
            authorities.add(role.getAuthority());

        try {
            var token = Jwts.builder()
                    .setIssuer("auth-service")
                    .setSubject(user.getId().toString())
                    .claim("authorities", authorities)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                    .compact();

            return new LoginResponseDTO(token, expiryDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PrivateKey getPrivateKey() throws Exception {
        StringBuilder keyBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(privateKeyResource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("-----BEGIN PRIVATE KEY-----") || line.contains("-----END PRIVATE KEY-----")) {
                    continue;
                }
                keyBuilder.append(line);
            }
        }

        byte[] keyBytes = Base64.getDecoder().decode(keyBuilder.toString());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(getPrivateKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return UUID.fromString(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getPrivateKey()).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}