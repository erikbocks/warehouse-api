package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface ProductService {

    Page<Product> findAllByOwner(Integer ownerId, Pageable pageable);

    void saveProduct(ProductRegisterDTO product, User owner);
}
