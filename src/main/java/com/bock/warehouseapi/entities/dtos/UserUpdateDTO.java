package com.bock.warehouseapi.entities.dtos;

import com.bock.warehouseapi.entities.User;
import jakarta.validation.constraints.*;

public class UserUpdateDTO {

    @NotNull(message = "O campo ID não pode ser nulo.")
    @Positive(message = "O campo ID não pode ser menor ou igual a zero.")
    private Integer id;

    @NotNull(message = "O campo USERNAME não pode ser nulo.")
    @NotBlank(message = "O campo USERNAME não pode estar em branco.")
    @NotEmpty(message = "O campo USERNAME não pode estar vazio.")
    private String username;

    @NotNull(message = "O campo EMAIL não pode ser nulo.")
    @NotBlank(message = "O campo EMAIL não pode estar em branco.")
    @NotEmpty(message = "O campo EMAIL não pode estar vazio.")
    @Email(message = "Insira um endereço de email válido")
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (this.id != null && this.id != 0) {
            user.setId(this.id);
        }
        if (this.email != null && !this.email.isBlank() && !this.email.isEmpty()) {
            user.setEmail(this.email);
        }
        if (this.username != null && !this.username.isBlank() && !this.username.isEmpty()) {
            user.setUsername(this.username);
        }

        return user;
    }
}
