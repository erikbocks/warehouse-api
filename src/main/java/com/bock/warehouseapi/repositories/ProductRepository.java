package com.bock.warehouseapi.repositories;

import com.bock.warehouseapi.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM products p INNER JOIN users u ON p.owner.id = :ownerId")
    Page<Product> findAllByOwnerId(@Param("ownerId") Integer id, Pageable pageable);
}
