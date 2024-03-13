package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.UserUpdateDTO;
import com.bock.warehouseapi.entities.dtos.UserPasswordDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(Integer id) throws InvalidDataException;

    Optional<User> findByUsername(String username);
    void updateUser(User dbUser, UserUpdateDTO reqUser) throws InvalidDataException;
    void updatePassword(UserPasswordDTO reqUser, User dbUser) throws InvalidDataException;

    void removeUser(Integer id) throws InvalidDataException;
}
