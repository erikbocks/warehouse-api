package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.entities.dtos.ProductUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<Product> findAllByOwner(Integer ownerId, Pageable pageable) throws InvalidDataException;

    void saveProduct(ProductRegisterDTO product, User owner);

    void updateProduct(ProductUpdateDTO reqProduct, User tokenUser) throws InvalidDataException;

    void deleteProduct(Integer productId, User tokenUser) throws InvalidDataException;
}
