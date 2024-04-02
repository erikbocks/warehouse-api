package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.entities.dtos.ProductUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.repositories.ProductRepository;
import com.bock.warehouseapi.services.impls.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository repository;
    @InjectMocks
    ProductServiceImpl service;

    User mockUser;
    Product product;
    ProductRegisterDTO toSaveProduct;
    ProductUpdateDTO updateProduct;

    @BeforeEach
    void setup() {
        mockUser = mock(User.class);

        product = new Product(1, "Sabonete", 6, "2,49", new Date(), null, mockUser);

        toSaveProduct = new ProductRegisterDTO("Shampoo", 1, "24,99");

        updateProduct = new ProductUpdateDTO(1, "Pasta de Dentes", 3, "5,65");
    }

    @Test
    @DisplayName("Should return product page.")
    void findAllByOwner_CaseSuccess() throws InvalidDataException {
        Integer ownerId = 1;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = mock(Page.class);

        when(repository.findAllByOwnerId(anyInt(), any(Pageable.class))).thenReturn(products);

        Page<Product> dbProducts = service.findAllByOwner(ownerId, pageable);
        verify(repository, times(1)).findAllByOwnerId(anyInt(), any(Pageable.class));

        assertThat(dbProducts).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Should throw exception because of invalid owner id.")
    void findAllByOwner_CaseInvalidId_ThrowsException(Integer ownerId) {
        Pageable pageable = PageRequest.of(0, 10);

        Exception thrown = assertThrows(InvalidDataException.class, () -> {
            Page<Product> dbProducts = service.findAllByOwner(ownerId, pageable);
        });

        verifyNoInteractions(repository);

        assertEquals("O parâmetro ID não pode ser igual ou menor do que zero.", thrown.getMessage());
    }

    @Test
    @DisplayName("Talvez nao precise desse")
    void saveProduct_CaseSuccess() {
        service.saveProduct(toSaveProduct, mockUser);
        verify(repository, times(1)).saveAndFlush(any(Product.class));
    }

    @Test
    @DisplayName("Should update product.")
    void updateProduct_CaseSuccess() throws InvalidDataException {

        when(repository.findByIdAndOwner(anyInt(), anyInt())).thenReturn(Optional.of(product));

        service.updateProduct(updateProduct, mockUser);

        verify(repository, times(1)).findByIdAndOwner(anyInt(), anyInt());
        verify(repository, times(1)).saveAndFlush(any(Product.class));
    }

    @Test
    @DisplayName("Should delete product.")
    void deleteProduct_CaseSuccess() throws InvalidDataException {
        Integer productId = 1;

        when(repository.findByIdAndOwner(anyInt(), anyInt())).thenReturn(Optional.of(product));

        service.deleteProduct(productId, mockUser);

        verify(repository, times(1)).findByIdAndOwner(anyInt(), anyInt());
        verify(repository, times(1)).delete(any(Product.class));
    }


    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Should thrown exception because of invalid id.")
    void deleteProduct_CaseInvalidId_ThrowsException(Integer productId) {
        Exception thrown = assertThrows(InvalidDataException.class, () -> {
          service.deleteProduct(productId, mockUser);
        });

        verifyNoInteractions(repository);

        assertEquals("O parâmetro ID não pode ser igual ou menor do que zero.", thrown.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because of not found product.")
    void deleteProduct_CaseProductNotFound_ThrowsException() {
        Integer productId = 999;
        Exception thrown = assertThrows(InvalidDataException.class, () -> {
            service.deleteProduct(productId, mockUser);
        });

        verify(repository, times(1)).findByIdAndOwner(anyInt(), anyInt());
        verifyNoMoreInteractions(repository);

        assertEquals("Nenhum produto encontrado com esse id.", thrown.getMessage());
    }
}