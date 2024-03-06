package com.bock.warehouseapi.entities.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserRegisterDTO {

    @NotNull(message = "O campo USERNAME não pode ser nulo.")
    @NotBlank(message = "O campo USERNAME não pode estar em branco.")
    @NotEmpty(message = "O campo USERNAME não pode estar vazio.")
    private String username;

    @NotNull(message = "O campo EMAIL não pode ser nulo.")
    @NotBlank(message = "O campo EMAIL não pode estar em branco.")
    @NotEmpty(message = "O campo EMAIL não pode estar vazio.")
    private String email;

    @NotNull(message = "O campo PASSWORD não pode ser nulo.")
    @NotBlank(message = "O campo PASSWORD não pode estar em branco.")
    @NotEmpty(message = "O campo PASSWORD não pode estar vazio.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
