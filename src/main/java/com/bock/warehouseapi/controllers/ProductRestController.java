package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.UserRole;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.entities.dtos.ProductUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.exceptions.InvalidRoleException;
import com.bock.warehouseapi.services.ProductService;
import com.bock.warehouseapi.services.TokenService;
import com.bock.warehouseapi.services.UserService;
import com.bock.warehouseapi.utils.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class ProductRestController {

    private final ProductService productService;
    private final UserService userService;
    private final RestResponse restResponse;

    private final TokenService tokenService;

    public ProductRestController(ProductService productService, UserService userService, RestResponse restResponse, TokenService tokenService) {
        this.productService = productService;
        this.userService = userService;
        this.restResponse = restResponse;
        this.tokenService = tokenService;
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
    public ResponseEntity<Object> saveProduct(@RequestBody @Valid ProductRegisterDTO product, HttpServletRequest request) throws InvalidDataException {
        try {
            String principalName = request.getUserPrincipal().getName();
            User tokenUser = userService.findByUsername(principalName).get();

            Optional<User> dbUser = userService.findById(product.getOwner());

            if (dbUser.isEmpty()) {
                return restResponse.badRequest("Nenhum usuário encontrado com esse ID.");
            }

            User user = dbUser.get();

            if (!user.getUsername().equals(tokenUser.getUsername()) && !tokenUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return restResponse.unauthorized("Você não tem permissão para realizar essa ação.");
            }

            productService.saveProduct(product, dbUser.get());

            return restResponse.created("Produto criado com sucesso.");
        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        } catch (Exception ex) {
            return restResponse.badRequest(ex.getMessage());
        }
    }

    @PutMapping("/products")
    public ResponseEntity<Object> updateProduct(@RequestBody @Valid ProductUpdateDTO reqProduct, HttpServletRequest request) throws InvalidDataException, InvalidRoleException {
        try {
            String principalName = request.getUserPrincipal().getName();

            User tokenUser = userService.findByUsername(principalName).get();

            productService.updateProduct(reqProduct, tokenUser);

            return restResponse.ok("Produto atualizado com sucesso.");

        } catch (InvalidDataException ex) {
            throw new InvalidDataException(ex.getMessage());
        } catch (InvalidRoleException ex) {
            throw new InvalidRoleException(ex.getMessage());
        }
    }
}
