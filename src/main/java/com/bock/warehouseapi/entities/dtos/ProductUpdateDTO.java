package com.bock.warehouseapi.entities.dtos;

import com.bock.warehouseapi.entities.Product;
import com.bock.warehouseapi.entities.User;
import jakarta.validation.constraints.*;

public class ProductUpdateDTO {

    @NotNull(message = "O campo ID não pode ser nulo.")
    @Positive(message = "O campo ID deve ser maior que zero.")
    private Integer id;

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

    public ProductUpdateDTO(Integer id, String productName, Integer amount, String value) {
        this.id = id;
        this.productName = productName;
        this.amount = amount;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Product toEntity(Product dbProduct) {
        if (!this.productName.trim().equals(dbProduct.getProductName())) {
            dbProduct.setProductName(this.productName.trim());
        }
        if (!this.amount.equals(dbProduct.getAmount())) {
            dbProduct.setAmount(this.amount);
        }
        if (!this.value.equals(dbProduct.getValue())) {
            dbProduct.setValue(this.value);
        }

        return dbProduct;
    }
}
