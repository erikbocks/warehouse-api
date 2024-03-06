package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.exceptions.InvalidTokenException;

public interface TokenService {
    String generateToken(User user);

    String validateToken(String token) throws InvalidTokenException;
}
