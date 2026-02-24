package com.orderGestion.orderGestion.application.usecase;

import com.orderGestion.orderGestion.application.port.OrderRepository;
import com.orderGestion.orderGestion.domain.model.Order;
import reactor.core.publisher.Mono;

public class FindOrderByIdUsCase {
    private final OrderRepository orderRepository;

    public FindOrderByIdUsCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Mono<Order> findById(int id) {

        return orderRepository.findById(id);
    }
}
