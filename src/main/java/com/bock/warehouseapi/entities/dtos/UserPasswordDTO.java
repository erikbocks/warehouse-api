package com.bock.warehouseapi.entities.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserPasswordDTO {

    @NotNull(message = "O campo OLDPASSWORD não pode ser nulo.")
    @NotBlank(message = "O campo OLDPASSWORD não pode estar em branco.")
    @NotEmpty(message = "O campo OLDPASSWORD não pode estar vazio.")
    private String oldPassword;

    @NotNull(message = "O campo NEWPASSWORD não pode ser nulo.")
    @NotBlank(message = "O campo NEWPASSWORD não pode estar em branco.")
    @NotEmpty(message = "O campo NEWPASSWORD não pode estar vazio.")
    private String newPassword;

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
