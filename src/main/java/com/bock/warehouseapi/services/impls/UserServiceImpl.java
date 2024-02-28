package com.bock.warehouseapi.services.impls;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.repositories.UserRepository;
import com.bock.warehouseapi.services.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return repository.findById(id);
    }
}
