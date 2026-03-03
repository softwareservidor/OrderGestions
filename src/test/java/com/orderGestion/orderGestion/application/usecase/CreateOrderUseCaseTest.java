package com.orderGestion.orderGestion.application.usecase;

import com.orderGestion.orderGestion.application.port.OrderRepository;
import com.orderGestion.orderGestion.domain.model.Costumer;
import com.orderGestion.orderGestion.domain.model.Order;
import com.orderGestion.orderGestion.domain.model.OrderStatus;
import com.orderGestion.orderGestion.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    private CreateOrderUseCase createOrderUseCase;

    private Order order;

    @BeforeEach
    void setUp() {
        createOrderUseCase = new CreateOrderUseCase(orderRepository);
        Costumer costumer = new Costumer(1, "Juan", "juan@mail.com");
        List<Product> products = List.of(
                new Product(1, "Producto A", new BigDecimal("100.00")),
                new Product(2, "Producto B", new BigDecimal("200.00"))
        );
        order = new Order(1, costumer, products, new BigDecimal("300.00"), OrderStatus.CREATED);
    }

    @Test
    void createOrder_deberiaGuardarLaOrdenCorrectamente() {
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(order));

        Mono<Order> result = createOrderUseCase.createOrder(order);

        StepVerifier.create(result)
                .expectNext(order)
                .verifyComplete();

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void createOrder_cuandoRepositorioFalla_deberiaPropagarError() {
        when(orderRepository.save(any(Order.class)))
                .thenReturn(Mono.error(new RuntimeException("Error al guardar")));

        Mono<Order> result = createOrderUseCase.createOrder(order);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof RuntimeException && e.getMessage().equals("Error al guardar"))
                .verify();

        verify(orderRepository, times(1)).save(order);
    }
}
