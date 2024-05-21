package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.UserPasswordDTO;
import com.bock.warehouseapi.entities.dtos.UserUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.services.UserService;
import com.bock.warehouseapi.utils.RestErrorResponse;
import com.bock.warehouseapi.utils.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "Endpoints relacionadas ao usuário.")
@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class UserRestController {
    private final RestResponse restResponse;
    private final UserService userService;

    public UserRestController(RestResponse restResponse, UserService userService) {
        this.restResponse = restResponse;
        this.userService = userService;
    }

    @Operation(summary="Encontra o usuário.", description = "Retorna o usuário vinculado ao token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falha na autenticação via token JWT.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/users/find")
    public ResponseEntity<Object> findUserById(HttpServletRequest request) throws InvalidDataException {
        String principalName = request.getUserPrincipal().getName();
        User tokenUser = userService.findByUsername(principalName);

        User owner = new User(tokenUser.getId(), tokenUser.getUsername(), tokenUser.getEmail(), tokenUser.getRole());

        return restResponse.ok("Usuário encontrado.", owner);
    }

    @Operation(summary = "Atualiza o usuário.", description = "Atualiza os campos email e username do usuário vinculado ao token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar usuário.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falha na autenticação via token JWT.", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/users/update")
    public ResponseEntity<Object> updateUser(HttpServletRequest request, @RequestBody @Valid UserUpdateDTO user) throws InvalidDataException {
        String principalName = request.getUserPrincipal().getName();
        User tokenUser = userService.findByUsername(principalName);

        User dbUser = userService.findById(tokenUser.getId());

        userService.updateUser(dbUser, user);

        return restResponse.ok("Usuário atualizado com sucesso.");
    }

    @Operation(summary = "Atualiza a senha do usuário.", description = "Atualiza a senha do usuário vinculado ao token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar senha.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falha na autenticação via token JWT.", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/users/update-password")
    public ResponseEntity<Object> updateUserPassword(@RequestBody @Valid UserPasswordDTO user, HttpServletRequest request) throws InvalidDataException {
        String principalName = request.getUserPrincipal().getName();
        User tokenUser = userService.findByUsername(principalName);

        User dbUser = userService.findById(tokenUser.getId());

        userService.updatePassword(user, dbUser);

        return restResponse.ok("Senha atualizada com sucesso.");
    }

    @Operation(summary = "Remove o usuário", description = "Remove o usuário vinculado ao token e todos os produtos associados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário removido com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao remover usuário.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falha na autenticação via token JWT.", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/users")
    public ResponseEntity<Object> removeUser(HttpServletRequest request) throws InvalidDataException {
        String principalName = request.getUserPrincipal().getName();
        User tokenUser = userService.findByUsername(principalName);

        userService.removeUser(tokenUser.getId());

        return restResponse.ok("Usuário removido com sucesso.");


    }
}
