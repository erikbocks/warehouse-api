package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.User;

public interface TokenService {
    String generateToken(User user);

    Integer validateToken(String token);
}
