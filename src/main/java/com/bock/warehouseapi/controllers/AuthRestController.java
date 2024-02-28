package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.LoginResponseDTO;
import com.bock.warehouseapi.entities.dtos.UserLoginDTO;
import com.bock.warehouseapi.entities.dtos.UserRegisterDTO;
import com.bock.warehouseapi.repositories.UserRepository;
import com.bock.warehouseapi.services.impls.TokenServiceImpl;
import com.bock.warehouseapi.utils.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final RestResponse restResponse;
    private final TokenServiceImpl tokenService;

    public AuthRestController(AuthenticationManager authenticationManager, UserRepository repository, RestResponse restResponse, TokenServiceImpl tokenService) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.restResponse = restResponse;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserRegisterDTO newUser) {
        if (repository.findBySubject(newUser.email()) != null) {
            return restResponse.badRequest("Email já cadastrado.");
        } else if (repository.findBySubject(newUser.username()) != null) {
            return restResponse.badRequest("Nome de usuário já cadastrado.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(newUser.password());
        User toSaveUser = new User(newUser.username(), newUser.email(), encryptedPassword);

        this.repository.saveAndFlush(toSaveUser);

        return restResponse.created("Usuário criado com sucesso.");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO user) {

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(user.login(), user.password());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) auth.getPrincipal());

        return restResponse.ok("Usuário autenticado com sucesso.", new LoginResponseDTO(token));
    }
}
