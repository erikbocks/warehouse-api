package com.bock.warehouseapi.entities;

import jakarta.persistence.*;

import java.util.Date;

@Table(name = "products")
@Entity(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "amount")
    private Integer amount;
    @Column(name = "value")
    private String value;
    @Column(name = "created_on")
    private Date createdOn;
    @Column(name = "last_edit")
    private Date lastEdit;
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    public Product() {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @PrePersist
    void setCreatedOn() {
        this.createdOn = new Date();
    }

    @PreUpdate
    void setLastEdit() {this.lastEdit = new Date();}

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
