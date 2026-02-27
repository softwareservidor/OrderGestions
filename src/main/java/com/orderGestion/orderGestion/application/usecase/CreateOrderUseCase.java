package com.orderGestion.orderGestion.application.usecase;


import com.orderGestion.orderGestion.application.port.OrderRepository;
import com.orderGestion.orderGestion.domain.model.Order;
import reactor.core.publisher.Mono;

public class CreateOrderUseCase {
    private final OrderRepository jpaOrderRepository;

    public CreateOrderUseCase(OrderRepository orderRepository) {
        this.jpaOrderRepository = orderRepository;
    }


    public Mono<Order> createOrder(Order order) {
        return jpaOrderRepository.save(order);
    }

}
