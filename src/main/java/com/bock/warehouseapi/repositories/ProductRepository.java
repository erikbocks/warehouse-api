package com.bock.warehouseapi.repositories;

import com.bock.warehouseapi.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select p from products p " +
            "INNER JOIN p.owner o " +
            "WHERE p.id = :productId " +
            "AND o.id = :ownerId")
    Optional<Product> findByIdAndOwner(@Param("productId") Integer id, @Param("ownerId") Integer ownerId);

    @Query("SELECT p FROM products p " +
            "INNER JOIN p.owner o " +
            "WHERE o.id = :ownerId " +
            "ORDER BY p.id ASC")
    Page<Product> findAllByOwnerId(@Param("ownerId") Integer id, Pageable pageable);
}
