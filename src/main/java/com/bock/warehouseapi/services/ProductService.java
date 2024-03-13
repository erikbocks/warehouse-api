package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.entities.dtos.ProductUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.exceptions.InvalidRoleException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductService {

    Optional<Product> findById(Integer id);

    Page<Product> findAllByOwner(Integer ownerId, Pageable pageable);

    void saveProduct(ProductRegisterDTO product, User owner);

    void updateProduct(ProductUpdateDTO reqProduct, User tokenUser) throws InvalidDataException, InvalidRoleException;
}
