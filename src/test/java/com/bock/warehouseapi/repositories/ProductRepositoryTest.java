package com.bock.warehouseapi.repositories;

import com.bock.warehouseapi.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    ProductRepository repository;

    @Test
    @DisplayName("Should get one product using both user and product id.")
    void findByIdAndOwnerCaseSuccess() {
        Integer userId = 1;
        Integer productId = 1;

        Optional<Product> dbProducts = repository.findByIdAndOwner(userId, productId);

        assertThat(dbProducts).isNotNull();
        assertThat(dbProducts.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get one product using both user and product id.")
    void findByIdAndOwnerCaseFailure() {
        Integer userId = 1;
        Integer productId = 999;

        Optional<Product> dbProducts = repository.findByIdAndOwner(userId, productId);

        assertThat(dbProducts).isNotNull();
        assertThat(dbProducts.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should get product page using the owner id.")
    void findAllByOwnerIdCaseSuccess() {
        Integer ownerId = 1;

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> dbProducts = repository.findAllByOwnerId(ownerId, pageable);

        assertThat(dbProducts).isNotNull();
        assertThat(dbProducts.getTotalElements()).isNotZero();
    }

    @Test
    @DisplayName("Should not get product page using the owner id.")
    void findAllByOwnerIdCaseFailure() {
        Integer ownerId = 999;

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> dbProducts = repository.findAllByOwnerId(ownerId, pageable);

        assertThat(dbProducts).isNotNull();
        assertThat(dbProducts.getTotalElements()).isZero();
    }
}