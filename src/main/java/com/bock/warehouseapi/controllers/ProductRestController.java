package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.services.ProductService;
import com.bock.warehouseapi.services.UserService;
import com.bock.warehouseapi.utils.RestResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class ProductRestController {

    private final ProductService productService;
    private final UserService userService;
    private final RestResponse restResponse;

    public ProductRestController(ProductService productService, UserService userService, RestResponse restResponse) {
        this.productService = productService;
        this.userService = userService;
        this.restResponse = restResponse;
    }

    @GetMapping(value = "/products/{id}")
    public ResponseEntity<Object> findAllByOwner(@PathVariable Integer id, @PageableDefault(size = 10) Pageable pageable) throws InvalidDataException {
        try {
            Optional<User> dbUser = userService.findById(id);

            if (dbUser.isEmpty()) {
                return restResponse.badRequest("Nenhum usuário encontrado com esse ID.");
            }

            Page<Product> products = productService.findAllByOwner(id, pageable);

            return restResponse.ok("Produtos encontrados com sucesso.", products);
        } catch (InvalidDataException e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    @PostMapping(value = "/products")
    public ResponseEntity<Object> saveProduct(@RequestBody @Valid ProductRegisterDTO product) throws InvalidDataException {
        try {
            Optional<User> dbUser = userService.findById(product.getOwner());

            if (dbUser.isEmpty()) {
                return restResponse.badRequest("Nenhum usuário encontrado com esse ID.");
            }

            productService.saveProduct(product, dbUser.get());

            return restResponse.created("Produto criado com sucesso.");
        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        } catch (Exception ex) {
            return restResponse.badRequest(ex.getMessage());
        }
    }
}
