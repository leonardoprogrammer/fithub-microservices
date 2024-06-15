package com.fithub.user_service.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_login")
public class UserLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "date_inc", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateInc;

    @Column(name = "date_alt")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateAlt;

    public UserLogin() {
    }

    public UserLogin(UUID userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.dateInc = Timestamp.valueOf(LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getDateInc() {
        return dateInc;
    }

    public void setDateInc(Timestamp dateInc) {
        this.dateInc = dateInc;
    }

    public Timestamp getDateAlt() {
        return dateAlt;
    }

    public void setDateAlt(Timestamp dateAlt) {
        this.dateAlt = dateAlt;
    }

    public void setDateAltDefault() {
        this.dateAlt = Timestamp.valueOf(LocalDateTime.now());
    }
}
