package com.fithub.workout_service.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(name = "date_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateBirth;

    @Column(length = 10)
    private String gender;

    @Column(length = 1, nullable = false)
    private String status;

    @Column(name = "date_inc", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateInc;

    @Column(name = "date_alt")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateAlt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
