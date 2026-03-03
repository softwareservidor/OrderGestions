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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindOrderByIdUsCaseTest {

    @Mock
    private OrderRepository orderRepository;

    private FindOrderByIdUsCase findOrderByIdUsCase;

    private Order order;

    @BeforeEach
    void setUp() {
        findOrderByIdUsCase = new FindOrderByIdUsCase(orderRepository);
        Costumer costumer = new Costumer(1, "Juan", "juan@mail.com");
        List<Product> products = List.of(
                new Product(1, "Producto A", new BigDecimal("100.00"))
        );
        order = new Order(1, costumer, products, new BigDecimal("100.00"), OrderStatus.CREATED);
    }

    @Test
    void findById_cuandoOrdenExiste_deberiaRetornarOrden() {
        when(orderRepository.findById(1)).thenReturn(Mono.just(order));

        Mono<Order> result = findOrderByIdUsCase.findById(1);

        StepVerifier.create(result)
                .expectNext(order)
                .verifyComplete();

        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    void findById_cuandoOrdenNoExiste_deberiaRetornarVacio() {
        when(orderRepository.findById(99)).thenReturn(Mono.empty());

        Mono<Order> result = findOrderByIdUsCase.findById(99);

        StepVerifier.create(result)
                .verifyComplete();

        verify(orderRepository, times(1)).findById(99);
    }

    @Test
    void findById_cuandoRepositorioFalla_deberiaPropagarError() {
        when(orderRepository.findById(1))
                .thenReturn(Mono.error(new RuntimeException("Error de conexión")));

        Mono<Order> result = findOrderByIdUsCase.findById(1);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof RuntimeException && e.getMessage().equals("Error de conexión"))
                .verify();

        verify(orderRepository, times(1)).findById(1);
    }
}
