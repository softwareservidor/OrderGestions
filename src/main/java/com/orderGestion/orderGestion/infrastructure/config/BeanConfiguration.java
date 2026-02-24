package com.orderGestion.orderGestion.infrastructure.config;

import com.orderGestion.orderGestion.application.port.OrderRepository;
import com.orderGestion.orderGestion.application.usecase.CreateOrderUseCase;
import com.orderGestion.orderGestion.application.usecase.FindOrderByIdUsCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CreateOrderUseCase createReservationUseCase(OrderRepository orderRepository) {
        return new CreateOrderUseCase(orderRepository);
    }

    @Bean
    public FindOrderByIdUsCase findOrderByIdUsCase(OrderRepository orderRepository) {
        return new FindOrderByIdUsCase(orderRepository);
    }

}
