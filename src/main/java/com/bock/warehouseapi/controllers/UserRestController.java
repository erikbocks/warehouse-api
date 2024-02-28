package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.services.UserService;
import com.bock.warehouseapi.utils.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/users")
public class UserRestController {
    private final RestResponse restResponse;
    private final UserService userService;

    public UserRestController(RestResponse restResponse, UserService userService) {
        this.restResponse = restResponse;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable Integer id) {

        Optional<User> dbUser = userService.findById(id);

        if (dbUser.isEmpty()) {
            return restResponse.badRequest("Nenhum usuário encontrado.");
        }

        return restResponse.ok("Usuário encontrado.", dbUser.get());
    }
}
