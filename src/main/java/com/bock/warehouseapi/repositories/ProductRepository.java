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
            "WHERE p.id = ?1 " +
            "AND o.id = ?2")
    Optional<Product> findByIdAndOwner(Integer id, Integer ownerId);

    @Query("SELECT p FROM products p " +
            "INNER JOIN users u " +
            "ON p.owner.id = :ownerId")
    Page<Product> findAllByOwnerId(@Param("ownerId") Integer id, Pageable pageable);
}
