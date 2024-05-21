package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.LoginResponseDTO;
import com.bock.warehouseapi.entities.dtos.UserLoginDTO;
import com.bock.warehouseapi.entities.dtos.UserRegisterDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.services.TokenService;
import com.bock.warehouseapi.services.impls.AuthorizationServiceImpl;
import com.bock.warehouseapi.utils.RestResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthRestController {
    private final RestResponse restResponse;
    private final AuthorizationServiceImpl authService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthRestController(RestResponse restResponse, AuthorizationServiceImpl authService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.restResponse = restResponse;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid UserRegisterDTO newUser) throws InvalidDataException {

        String message = authService.validateRegex(newUser);

        if (!message.isEmpty()) {
            throw new InvalidDataException(message);
        }

        authService.registerUser(newUser);

        return restResponse.created("Usuário criado com sucesso.");

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO user) {

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(user.getLogin().trim(), user.getPassword().trim());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return restResponse.ok("Usuário autenticado com sucesso.", new LoginResponseDTO(((User) auth.getPrincipal()).getId(), token));
    }
}
