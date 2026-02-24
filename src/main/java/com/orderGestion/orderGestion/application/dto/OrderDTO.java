package com.orderGestion.orderGestion.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    private int orderId;
    private CostumerDTO costumerId;
    private List<ProductDTO> products;
    private BigDecimal totalAmount;
    private String orderStatus;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public CostumerDTO getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(CostumerDTO costumerId) {
        this.costumerId = costumerId;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
