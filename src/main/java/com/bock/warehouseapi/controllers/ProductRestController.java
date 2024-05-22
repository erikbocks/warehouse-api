package com.bock.warehouseapi.controllers;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.entities.dtos.ProductUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.services.ProductService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Products", description = "Endpoints relacionadas aos produtos.")
@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class ProductRestController {

    private final ProductService productService;
    private final UserService userService;
    private final RestResponse restResponse;

    public ProductRestController(ProductService productService, UserService userService, RestResponse restResponse) {
        this.productService = productService;
        this.userService = userService;
        this.restResponse = restResponse;
    }

    @Operation(summary = "Encontra todos os produtos.", description = "Retorna todos os produtos vinculados ao usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falha na autenticação via token JWT.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "/products")
    public ResponseEntity<Object> findAllByOwner(HttpServletRequest request, @PageableDefault(size = 5) Pageable pageable) throws InvalidDataException {
        String principalName = request.getUserPrincipal().getName();
        User tokenUser = userService.findByUsername(principalName);

        Page<Product> products = productService.findAllByOwner(tokenUser.getId(), pageable);

        return restResponse.ok("Produtos encontrados com sucesso.", products);
    }

    @Operation(summary = "Salva um novo produto.", description = "Salva um produto no banco de dados, vinculando o usuário do token como dono.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao salvar produto. Pode ser relativo aos dados passados na requisição (já existente no banco, tipos inválidos, etc).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falha na autenticação via token JWT.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = "/products")
    public ResponseEntity<Object> saveProduct(HttpServletRequest request, @RequestBody @Valid ProductRegisterDTO product) throws InvalidDataException {
        String principalName = request.getUserPrincipal().getName();
        User tokenUser = userService.findByUsername(principalName);

        productService.saveProduct(product, tokenUser);

        return restResponse.created("Produto criado com sucesso.");
    }

    @Operation(summary = "Atualiza um produto.", description = "Atualiza o produto vinculado ao usuario procurando pelo ID da requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar produto. Pode ser relativo aos dados passados na requisição (já existente no banco, não encontrado, tipos inválidos, etc).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falha na autenticação via token JWT.", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/products")
    public ResponseEntity<Object> updateProduct(@RequestBody @Valid ProductUpdateDTO reqProduct, HttpServletRequest request) throws InvalidDataException {
        String principalName = request.getUserPrincipal().getName();
        User tokenUser = userService.findByUsername(principalName);

        productService.updateProduct(reqProduct, tokenUser);

        return restResponse.ok("Produto atualizado com sucesso.");

    }

    @Operation(summary = "Deleta um produto.", description = "Deleta o produto vinculado ao usuário procurando pelo ID da URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto removido com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao remover produto. Pode ser relativo aos dados passados na requisição (não encontrado no banco, tipos inválidos, etc).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Falha na autenticação via token JWT.", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Object> deleteProduct(HttpServletRequest request, @PathVariable Integer productId) throws InvalidDataException {
        String principalName = request.getUserPrincipal().getName();
        User tokenUser = userService.findByUsername(principalName);

        productService.deleteProduct(productId, tokenUser);

        return restResponse.ok("Produto removido com sucesso.");
    }
}
