package com.bock.warehouseapi.services.impls;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.UserLoginDTO;
import com.bock.warehouseapi.entities.dtos.UserRegisterDTO;
import com.bock.warehouseapi.exceptions.custom.InvalidDataException;
import com.bock.warehouseapi.repositories.UserRepository;
import com.bock.warehouseapi.services.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements UserDetailsService {

    private final UserRepository repository;
    public AuthorizationServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return repository.findBySubject(username);
        } catch (Exception ex) {
            return null;
        }
    }

    public void registerUser(UserRegisterDTO userData) throws InvalidDataException {
        if (repository.findBySubject(userData.email()) != null) {
            throw new InvalidDataException("Email já cadastrado.");
        } else if (repository.findBySubject(userData.username()) != null) {
            throw new InvalidDataException("Nome de usuário já cadastrado.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userData.password());
        User toSaveUser = new User(userData.username(), userData.email(), encryptedPassword);

        this.repository.saveAndFlush(toSaveUser);
    }
}
