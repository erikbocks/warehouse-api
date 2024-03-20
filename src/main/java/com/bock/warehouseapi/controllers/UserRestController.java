package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.UserPasswordDTO;
import com.bock.warehouseapi.entities.dtos.UserUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.services.UserService;
import com.bock.warehouseapi.utils.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class UserRestController {
    private final RestResponse restResponse;
    private final UserService userService;

    public UserRestController(RestResponse restResponse, UserService userService) {
        this.restResponse = restResponse;
        this.userService = userService;
    }

    @GetMapping("/users/find")
    public ResponseEntity<Object> findUserById(HttpServletRequest request) throws InvalidDataException {
        try {
            String principalName = request.getUserPrincipal().getName();
            User tokenUser = userService.findByUsername(principalName).get();

            User dbUser = userService.findById(tokenUser.getId());

            return restResponse.ok("Usuário encontrado.", dbUser);
        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        }
    }

    @PutMapping("/users/update")
    public ResponseEntity<Object> updateUser(HttpServletRequest request, @RequestBody @Valid UserUpdateDTO user) throws InvalidDataException {
        try {
            String principalName = request.getUserPrincipal().getName();
            User tokenUser = userService.findByUsername(principalName).get();

            User dbUser = userService.findById(tokenUser.getId());

            userService.updateUser(dbUser, user);

            return restResponse.ok("Usuário atualizado com sucesso.");
        } catch (InvalidDataException exception) {
            throw new InvalidDataException(exception.getMessage());
        }
    }

    @PutMapping("/users/update-password")
    public ResponseEntity<Object> updateUserPassword(@RequestBody @Valid UserPasswordDTO user, HttpServletRequest request) throws InvalidDataException {
        try {
            String principalName = request.getUserPrincipal().getName();
            User tokenUser = userService.findByUsername(principalName).get();

            User dbUser = userService.findById(tokenUser.getId());

            userService.updatePassword(user, dbUser);

            return restResponse.ok("Senha atualizada com sucesso.");
        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> removeUser(HttpServletRequest request, @PathVariable Integer id) throws InvalidDataException {
        try {
            String principalName = request.getUserPrincipal().getName();
            User tokenUser = userService.findByUsername(principalName).get();

            userService.removeUser(tokenUser.getId());

            return restResponse.ok("Usuário removido com sucesso.");
        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        }
    }
}
