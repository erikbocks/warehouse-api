package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.UpdateUserDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(Integer id) throws InvalidDataException;

    void updateUser(User dbUser, UpdateUserDTO reqUser) throws InvalidDataException;
}
