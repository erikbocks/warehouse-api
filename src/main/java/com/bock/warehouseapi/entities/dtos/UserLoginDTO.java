package com.bock.warehouseapi.entities.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserLoginDTO {

    @NotNull(message = "O campo LOGIN não pode ser nulo.")
    @NotBlank(message = "O campo LOGIN não pode estar em branco.")
    @NotEmpty(message = "O campo LOGIN não pode estar vazio.")
    String login;

    @NotNull(message = "O campo PASSWORD não pode ser nulo.")
    @NotBlank(message = "O campo PASSWORD não pode estar em branco.")
    @NotEmpty(message = "O campo PASSWORD não pode estar vazio.")
    String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
