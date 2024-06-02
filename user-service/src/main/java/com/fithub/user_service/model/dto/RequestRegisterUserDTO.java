package com.fithub.user_service.model.dto;

import java.sql.Date;

public class RequestRegisterUserDTO {

    private String name;
    private Date dateBirth;
    private String gender;
    private RegisterLoginDTO login;

    public RequestRegisterUserDTO() {
    }

    public RequestRegisterUserDTO(String name, Date dateBirth, String gender, RegisterLoginDTO login) {
        this.name = name;
        this.dateBirth = dateBirth;
        this.gender = gender;
        this.login = login;
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

    public RegisterLoginDTO getLogin() {
        return login;
    }

    public void setLogin(RegisterLoginDTO login) {
        this.login = login;
    }
}
