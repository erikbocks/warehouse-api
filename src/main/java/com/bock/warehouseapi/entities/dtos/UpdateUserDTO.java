package com.bock.warehouseapi.entities.dtos;

import com.bock.warehouseapi.entities.User;

public class UpdateUserDTO {
    private Integer id;
    private String username;
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
