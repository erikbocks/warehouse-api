package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.utils.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users")
public class   UserRestController {

    private final RestResponse restResponse;

    public UserRestController(RestResponse restResponse) {
        this.restResponse = restResponse;
    }

    @GetMapping("/hello")
    public ResponseEntity<Object> test() {
        return restResponse.ok("Ola, mundo");
    }
}
