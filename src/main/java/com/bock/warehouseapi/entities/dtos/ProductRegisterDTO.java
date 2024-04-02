package com.bock.warehouseapi.entities.dtos;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductRegisterDTO {

    @NotNull(message = "O campo PRODUCTNAME não pode ser nulo.")
    @NotBlank(message = "O campo PRODUCTNAME não pode estar em branco.")
    @NotEmpty(message = "O campo PRODUCTNAME não pode estar vazio.")
    private String productName;

    @NotNull(message = "O campo AMOUNT não pode ser nulo.")
    @PositiveOrZero(message = "O campo AMOUNT não pode ser menor que zero.")
    private Integer amount;

    @NotNull(message = "O campo VALUE não pode ser nulo.")
    @NotBlank(message = "O campo VALUE não pode estar em branco.")
    @NotEmpty(message = "O campo VALUE não pode estar vazio.")
    private String value;

    public ProductRegisterDTO(String productName, Integer amount, String value) {
        this.productName = productName;
        this.amount = amount;
        this.value = value;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Product toEntity(User owner) {
        Product product = new Product();

        product.setProductName(this.productName);
        product.setAmount(this.amount);
        product.setValue(this.value);
        product.setOwner(owner);

        return product;
    }
}
