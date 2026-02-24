package com.orderGestion.orderGestion.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {

    private int orderId;
    private Costumer costumerId;
    private List<Product> products;
    private BigDecimal totalAmount;
    private String orderStatus;

    public Order(int orderId, Costumer costumerId, List<Product> products, BigDecimal totalAmount, String orderStatus) {
        this.orderId = orderId;
        this.costumerId = costumerId;
        this.products = products;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
    }

    public Order() {
        
    }

    private void validateProducts(){
        if(products == null || products.isEmpty()){
            throw new IllegalArgumentException("La orden debe contener al menos un producto.");
        }
    }

    public void confirmOrder(){
        validateProducts();
        this.orderStatus = OrderStatus.CONFIRMED;
    }

    public void cancelOrder(){
        if(this.orderStatus == OrderStatus.CONFIRMED){
            throw new IllegalStateException("No se puede cancelar una orden confirmada.");
        }
        this.orderStatus = OrderStatus.CANCELLED;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Costumer getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(Costumer costumerId) {
        this.costumerId = costumerId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
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
