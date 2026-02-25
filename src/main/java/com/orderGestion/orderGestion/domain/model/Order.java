package com.orderGestion.orderGestion.domain.model;

import com.orderGestion.orderGestion.infrastructure.exception.InvalidDataException;

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

    public void calculateTotalAmount(){
        this.totalAmount = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateProducts(){
        if(products == null || products.isEmpty()){
            throw new InvalidDataException("La orden debe contener al menos un producto.");
        }
    }

    public void confirmOrder(){
        validateProducts();
        if(this.orderStatus.equals(OrderStatus.CREATED)){
            this.orderStatus = OrderStatus.CONFIRMED;
        }
        else {
            throw new InvalidDataException("Solo se pueden confirmar órdenes en estado 'CREATED'.");
        }
    }

    public void cancelOrder(){
        if(this.orderStatus.equals(OrderStatus.CREATED)){
            this.orderStatus = OrderStatus.CANCELLED;
        }else {
            throw new InvalidDataException("No se puede cancelar una orden confirmada.");
        }
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
