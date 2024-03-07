package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.UpdateUserDTO;
import com.bock.warehouseapi.entities.dtos.UserPasswordDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.services.UserService;
import com.bock.warehouseapi.utils.RestResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class UserRestController {
    private final RestResponse restResponse;
    private final UserService userService;

    public UserRestController(RestResponse restResponse, UserService userService) {
        this.restResponse = restResponse;
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<Object> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return restResponse.ok("Usuários encontrados", userService.findAll(pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable Integer id) {
        try {
            Optional<User> dbUser = userService.findById(id);

            if (dbUser.isEmpty()) {
                return restResponse.ok("Nenhum registro encontrado.");
            }

            return restResponse.ok("Usuário encontrado.", dbUser.get());
        } catch (Exception ex) {
            return restResponse.internalServerError("Ocorreu um erro ao processar sua requisição. Tente novamente mais tarde");
        }
    }

    @PutMapping("/users/update")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UpdateUserDTO user) throws InvalidDataException {
        try {
            Optional<User> dbUser = userService.findById(user.getId());

            if (dbUser.isEmpty()) {
                return restResponse.badRequest("Nenhum usuário encontrado com esse id.");
            }

            userService.updateUser(dbUser.get(), user);

            return restResponse.ok("Usuário atualizado com sucesso.");
        } catch (InvalidDataException exception) {
            throw new InvalidDataException(exception.getMessage());
        }
    }

    @PutMapping("/users/update-password")
    public ResponseEntity<Object> updateUserPassword(@RequestBody @Valid UserPasswordDTO user) throws InvalidDataException {
        try {
            Optional<User> dbUser = userService.findById(user.getId());

            if (dbUser.isEmpty()) {
                return restResponse.badRequest("Nenhum usuário encontrado com esse id.");
            }

            userService.updatePassword(user, dbUser.get());

            return restResponse.ok("Senha atualizada com sucesso.");
        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> removeUser(@PathVariable Integer id) throws InvalidDataException {
        try {
            Optional<User> dbUser = userService.findById(id);

            if (dbUser.isEmpty()) {
                return restResponse.badRequest("Nenhum usuário encontrado com esse id.");
            }

            userService.removeUser(id);

            return restResponse.ok("Usuário removido com sucesso.");
        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        }
    }
}
