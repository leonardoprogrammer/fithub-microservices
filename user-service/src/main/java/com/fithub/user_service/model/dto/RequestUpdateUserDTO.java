package com.fithub.user_service.model.dto;

import java.sql.Date;

public class RequestUpdateUserDTO {

    private String name;
    private Date dateBirth;
    private String gender;
    private String email;
    private String requiredPassword;

    public RequestUpdateUserDTO() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRequiredPassword() {
        return requiredPassword;
    }

    public void setRequiredPassword(String requiredPassword) {
        this.requiredPassword = requiredPassword;
    }
}
