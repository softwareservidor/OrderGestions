package com.orderGestion.orderGestion.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    private int productId;
    private String name;
    private double price;

    public ProductEntity(int productId, String name, double price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public ProductEntity() {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
