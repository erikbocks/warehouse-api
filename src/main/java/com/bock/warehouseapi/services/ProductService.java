package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.entities.dtos.ProductUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.exceptions.InvalidRoleException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Optional<Product> findById(Integer id);

    Page<Product> findAllByOwner(Integer ownerId, Pageable pageable, User tokenUser) throws InvalidDataException, InvalidRoleException;

    void saveProduct(ProductRegisterDTO product, User owner) throws InvalidDataException, InvalidRoleException;

    void updateProduct(ProductUpdateDTO reqProduct, User tokenUser) throws InvalidDataException, InvalidRoleException;

    void deleteProduct(Integer productId, User tokenUser) throws InvalidDataException, InvalidRoleException;
}
