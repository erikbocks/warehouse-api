package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.LoginResponseDTO;
import com.bock.warehouseapi.entities.dtos.UserLoginDTO;
import com.bock.warehouseapi.entities.dtos.UserRegisterDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.services.TokenService;
import com.bock.warehouseapi.services.impls.AuthorizationServiceImpl;
import com.bock.warehouseapi.utils.RestErrorResponse;
import com.bock.warehouseapi.utils.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoints de autenticação")
@RestController
@RequestMapping(value = "/api/auth", produces = "application/json")
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

    @Operation(summary = "Registra um novo usuário", description = "Cadastra um novo usuário no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro na criação do usuário. Pode ser relativo a credenciais já registradas, credenciais não passaram a validação, etc.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falha na autenticação via token JWT.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid UserRegisterDTO newUser) throws InvalidDataException {

        String message = authService.validateRegex(newUser);

        if (!message.isEmpty()) {
            throw new InvalidDataException(message);
        }

        authService.registerUser(newUser);

        return restResponse.created("Usuário criado com sucesso.");

    }

    @Operation(summary = "Autentica um usuário", description = "Autentica um usuário no sistema e retorna um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro na autenticação do usuário. Causado por credenciais inválidas.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falha na autenticação de usuário.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginDTO user) {

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(user.getLogin().trim(), user.getPassword().trim());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return restResponse.ok("Usuário autenticado com sucesso.", new LoginResponseDTO(((User) auth.getPrincipal()).getId(), token));
    }
}
