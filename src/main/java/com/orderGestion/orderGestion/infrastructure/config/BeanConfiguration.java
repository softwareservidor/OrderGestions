package com.orderGestion.orderGestion.infrastructure.config;

import com.orderGestion.orderGestion.application.port.OrderRepository;
import com.orderGestion.orderGestion.application.usecase.CreateOrderUseCase;
import com.orderGestion.orderGestion.application.usecase.FindOrderByIdUsCase;
import com.orderGestion.orderGestion.infrastructure.out.persistence.JPA.JpaRepositoryAdapter;
import com.orderGestion.orderGestion.infrastructure.out.persistence.JPA.SpringDataJPAOrderRepository;
import com.orderGestion.orderGestion.infrastructure.out.persistence.MONGO.MongoRepositoryAdapter;
import com.orderGestion.orderGestion.infrastructure.out.persistence.MONGO.SpringDataMongoOrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepository orderRepository) {
        return new CreateOrderUseCase(orderRepository);
    }

    @Bean
    public FindOrderByIdUsCase findOrderByIdUsCase(OrderRepository orderRepository) {
        return new FindOrderByIdUsCase(orderRepository);
    }

    @Bean
    public OrderRepository orderRepository(JpaRepositoryAdapter jpa, MongoRepositoryAdapter mongo) {
        return jpa;
    }
}
