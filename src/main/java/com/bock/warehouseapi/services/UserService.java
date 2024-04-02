package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.UserPasswordDTO;
import com.bock.warehouseapi.entities.dtos.UserUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;

public interface UserService {

    User findById(Integer userId) throws InvalidDataException;
    User findByUsername(String username) throws InvalidDataException;
    void updateUser(User dbUser, UserUpdateDTO reqUser) throws InvalidDataException;
    void updatePassword(UserPasswordDTO reqUser, User dbUser) throws InvalidDataException;
    void removeUser(Integer id) throws InvalidDataException;
}
