package com.bock.warehouseapi.entities.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UserPasswordDTO {

    @NotNull(message = "O campo ID não pode ser nulo.")
    @Positive(message = "O campo ID não pode ser menor ou igual a zero.")
    private Integer id;

    @NotNull(message = "O campo OLDPASSWORD não pode ser nulo.")
    @NotBlank(message = "O campo OLDPASSWORD não pode estar em branco.")
    @NotEmpty(message = "O campo OLDPASSWORD não pode estar vazio.")
    private String oldPassword;

    @NotNull(message = "O campo NEWPASSWORD não pode ser nulo.")
    @NotBlank(message = "O campo NEWPASSWORD não pode estar em branco.")
    @NotEmpty(message = "O campo NEWPASSWORD não pode estar vazio.")
    private String newPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
