package com.orderGestion.orderGestion.application.port;


import com.orderGestion.orderGestion.domain.model.Order;
import reactor.core.publisher.Mono;

public interface OrderRepository {
    Mono<Order> save(Order order);
    Mono<Order> findById(int id);
}
