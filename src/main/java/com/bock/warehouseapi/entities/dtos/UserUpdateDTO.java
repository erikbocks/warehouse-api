package com.bock.warehouseapi.entities.dtos;

import com.bock.warehouseapi.entities.User;
import jakarta.validation.constraints.*;

public class UserUpdateDTO {

    @NotNull(message = "O campo USERNAME não pode ser nulo.")
    @NotBlank(message = "O campo USERNAME não pode estar em branco.")
    @NotEmpty(message = "O campo USERNAME não pode estar vazio.")
    private String username;

    @NotNull(message = "O campo EMAIL não pode ser nulo.")
    @NotBlank(message = "O campo EMAIL não pode estar em branco.")
    @NotEmpty(message = "O campo EMAIL não pode estar vazio.")
    @Email(message = "Insira um endereço de email válido")
    private String email;

    public UserUpdateDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

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

    public User toEntity(User user) {
        if (this.email != null && !this.email.isBlank() && !this.email.isEmpty()) {
            user.setEmail(this.email.trim());
        }
        if (this.username != null && !this.username.isBlank() && !this.username.isEmpty()) {
            user.setUsername(this.username.trim());
        }

        return user;
    }
}
