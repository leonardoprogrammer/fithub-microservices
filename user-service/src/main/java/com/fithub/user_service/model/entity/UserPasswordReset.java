package com.fithub.user_service.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table
public class UserPasswordReset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_login_id", nullable = false)
    private UUID userLoginId;

    @Column(length = 255, nullable = false)
    private String email;

    @Column(name = "send_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp sendDate;

    @Column(length = 255, nullable = false)
    private String link;

    @Column(name = "date_expiration", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateExpiration;

    @Column(nullable = false)
    private Boolean reset;

    @Column(name = "reset_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp resetDate;

    @Column(name = "date_inc", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateInc;

    public UserPasswordReset() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(UUID userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Timestamp getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Timestamp dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Boolean getReset() {
        return reset;
    }

    public void setReset(Boolean reset) {
        this.reset = reset;
    }

    public Timestamp getResetDate() {
        return resetDate;
    }

    public void setResetDate(Timestamp resetDate) {
        this.resetDate = resetDate;
    }

    public Timestamp getDateInc() {
        return dateInc;
    }

    public void setDateInc(Timestamp dateInc) {
        this.dateInc = dateInc;
    }
}
