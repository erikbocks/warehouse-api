package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.LoginResponseDTO;
import com.bock.warehouseapi.entities.dtos.UserLoginDTO;
import com.bock.warehouseapi.entities.dtos.UserRegisterDTO;
import com.bock.warehouseapi.exceptions.custom.InvalidDataException;
import com.bock.warehouseapi.services.TokenService;
import com.bock.warehouseapi.services.impls.AuthorizationServiceImpl;
import com.bock.warehouseapi.utils.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
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
    public ResponseEntity<Object> register(@RequestBody UserRegisterDTO newUser) {
        try {
            authService.registerUser(newUser);

            return restResponse.created("Usuário criado com sucesso.");
        } catch (InvalidDataException exception) {
            return restResponse.badRequest(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO user) {

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(user.login(), user.password());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return restResponse.ok("Usuário autenticado com sucesso.", new LoginResponseDTO(token));
    }
}
