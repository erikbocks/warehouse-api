package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.entities.dtos.ProductUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.services.ProductService;
import com.bock.warehouseapi.services.UserService;
import com.bock.warehouseapi.utils.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/products")
    public ResponseEntity<Object> findAllByOwner(HttpServletRequest request, @PageableDefault(size = 5) Pageable pageable) throws InvalidDataException {
        try {
            String principalName = request.getUserPrincipal().getName();
            User tokenUser = userService.findByUsername(principalName);

            Page<Product> products = productService.findAllByOwner(tokenUser.getId(), pageable);

            return restResponse.ok("Produtos encontrados com sucesso.", products);
        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        }
    }

    @PostMapping(value = "/products")
    public ResponseEntity<Object> saveProduct(HttpServletRequest request, @RequestBody @Valid ProductRegisterDTO product) {
        try {
            String principalName = request.getUserPrincipal().getName();
            User tokenUser = userService.findByUsername(principalName);

            productService.saveProduct(product, tokenUser);

            return restResponse.created("Produto criado com sucesso.");
        } catch (Exception ex) {
            return restResponse.badRequest(ex.getMessage());
        }
    }

    @PutMapping("/products")
    public ResponseEntity<Object> updateProduct(@RequestBody @Valid ProductUpdateDTO reqProduct, HttpServletRequest request) throws InvalidDataException {
        try {
            String principalName = request.getUserPrincipal().getName();
            User tokenUser = userService.findByUsername(principalName);

            productService.updateProduct(reqProduct, tokenUser);

            return restResponse.ok("Produto atualizado com sucesso.");

        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Object> deleteProduct(HttpServletRequest request, @PathVariable Integer productId) throws InvalidDataException {
        try {
            String principalName = request.getUserPrincipal().getName();
            User tokenUser = userService.findByUsername(principalName);

            productService.deleteProduct(productId, tokenUser);

            return restResponse.ok("Produto removido com sucesso.");
        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        }
    }
}
