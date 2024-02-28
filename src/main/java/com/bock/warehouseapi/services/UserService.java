package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(Integer id);

}
