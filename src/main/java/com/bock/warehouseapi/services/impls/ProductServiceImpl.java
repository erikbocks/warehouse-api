package com.bock.warehouseapi.services.impls;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.entities.dtos.ProductUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.exceptions.InvalidRoleException;
import com.bock.warehouseapi.repositories.ProductRepository;
import com.bock.warehouseapi.repositories.UserRepository;
import com.bock.warehouseapi.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<Product> findAllByOwner(Integer ownerId, Pageable pageable) {
        return productRepository.findAllByOwnerId(ownerId, pageable);
    }

    @Override
    public void saveProduct(ProductRegisterDTO product, User owner) {
        if (product.getValue() == null || product.getValue().isBlank()) {
            product.setValue("0,00");
        }

        Product toSaveProduct = product.toEntity(owner);

        productRepository.saveAndFlush(toSaveProduct);
    }

    @Override
    public void updateProduct(ProductUpdateDTO reqProduct, User tokenUser) throws InvalidDataException, InvalidRoleException {

        Optional<Product> dbProduct = productRepository.findById(reqProduct.getId());

        if (dbProduct.isEmpty()) {
            throw new InvalidDataException("Nenhum produto encontrado com esse id");
        }

        Product product = dbProduct.get();

        if (!product.getOwner().getUsername().equals(tokenUser.getUsername()) && !tokenUser.isAdmin()) {
            throw new InvalidRoleException("Você não tem permissão para realizar essa ação.");
        }

        Product toUpdateProduct = reqProduct.toEntity(product);

        productRepository.saveAndFlush(toUpdateProduct);
    }
}
