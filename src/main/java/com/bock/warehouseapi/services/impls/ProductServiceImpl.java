package com.bock.warehouseapi.services.impls;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.repositories.ProductRepository;
import com.bock.warehouseapi.repositories.UserRepository;
import com.bock.warehouseapi.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
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
}
