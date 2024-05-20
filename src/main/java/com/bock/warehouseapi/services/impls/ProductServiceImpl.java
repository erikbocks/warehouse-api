package com.bock.warehouseapi.services.impls;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.ProductRegisterDTO;
import com.bock.warehouseapi.entities.dtos.ProductUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.repositories.ProductRepository;
import com.bock.warehouseapi.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findAllByOwner(Integer ownerId, Pageable pageable) throws InvalidDataException {
        if (ownerId <= 0) {
            throw new InvalidDataException("O parâmetro ID não pode ser igual ou menor do que zero.");
        }

        return productRepository.findAllByOwnerId(ownerId, pageable);
    }

    @Override
    public void saveProduct(ProductRegisterDTO product, User tokenUser) {
        if (product.getValue() == null || product.getValue().isBlank()) {
            product.setValue("0,00");
        }

        Product toSaveProduct = product.toEntity(tokenUser);

        productRepository.saveAndFlush(toSaveProduct);
    }

    @Override
    public void updateProduct(ProductUpdateDTO reqProduct, User tokenUser) throws InvalidDataException {

        Product dbProduct = productRepository.findByIdAndOwner(reqProduct.getId(), tokenUser.getId()).orElseThrow(() -> new InvalidDataException("Nenhum produto encontrado com esse id"));

        Product toUpdateProduct = reqProduct.toEntity(dbProduct);

        if (toUpdateProduct.getValue() == null || toUpdateProduct.getValue().isBlank()) {
            toUpdateProduct.setValue("0,00");
        }

        productRepository.saveAndFlush(toUpdateProduct);
    }

    @Override
    public void deleteProduct(Integer productId, User tokenUser) throws InvalidDataException {
        if (productId <= 0) {
            throw new InvalidDataException("O parâmetro ID não pode ser igual ou menor do que zero.");
        }

        Product dbProduct = productRepository.findByIdAndOwner(productId, tokenUser.getId()).orElseThrow(() -> new InvalidDataException("Nenhum produto encontrado com esse id."));

        productRepository.delete(dbProduct);
    }
}
