package com.orderGestion.orderGestion.infrastructure;

import com.orderGestion.orderGestion.application.dto.CostumerDTO;
import com.orderGestion.orderGestion.application.dto.OrderDTO;
import com.orderGestion.orderGestion.application.dto.ProductDTO;
import com.orderGestion.orderGestion.application.usecase.CreateOrderUseCase;
import com.orderGestion.orderGestion.application.usecase.FindOrderByIdUsCase;
import com.orderGestion.orderGestion.domain.model.Costumer;
import com.orderGestion.orderGestion.domain.model.Order;
import com.orderGestion.orderGestion.domain.model.OrderStatus;
import com.orderGestion.orderGestion.domain.model.Product;
import com.orderGestion.orderGestion.infrastructure.exception.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private FindOrderByIdUsCase findOrderByIdUsCase;

    private OrderController orderController;

    private OrderDTO orderDTO;
    private Order order;

    @BeforeEach
    void setUp() {
        orderController = new OrderController(createOrderUseCase, findOrderByIdUsCase);

        CostumerDTO costumerDTO = new CostumerDTO();
        costumerDTO.setCostumerId(1);
        costumerDTO.setName("Juan");
        costumerDTO.setEmail("juan@mail.com");

        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setProductId(1);
        productDTO1.setName("Producto A");
        productDTO1.setPrice(new BigDecimal("100.00"));

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setProductId(2);
        productDTO2.setName("Producto B");
        productDTO2.setPrice(new BigDecimal("200.00"));

        orderDTO = new OrderDTO();
        orderDTO.setOrderId(1);
        orderDTO.setCostumerId(costumerDTO);
        orderDTO.setProducts(List.of(productDTO1, productDTO2));
        orderDTO.setOrderStatus(OrderStatus.CREATED);


        Costumer costumer = new Costumer(1, "Juan", "juan@mail.com");
        List<Product> products = List.of(
                new Product(1, "Producto A", new BigDecimal("100.00")),
                new Product(2, "Producto B", new BigDecimal("200.00"))
        );
        order = new Order(1, costumer, products, new BigDecimal("300.00"), OrderStatus.CREATED);
    }

    // ===== Tests para create =====

    @Test
    void create_deberiaCrearOrdenYRetornar201() {
        when(createOrderUseCase.createOrder(any(Order.class))).thenReturn(Mono.just(order));

        Mono<ResponseEntity<ApiResponse<Order>>> result = orderController.create(orderDTO);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertNotNull(response.getBody());
                    assertEquals(201, response.getBody().getStatus());
                    assertEquals("Orden creada exitosamente", response.getBody().getMessage());
                    assertNotNull(response.getBody().getData());
                })
                .verifyComplete();

        verify(createOrderUseCase, times(1)).createOrder(any(Order.class));
    }

    @Test
    void create_cuandoFalla_deberiaRetornar400() {
        when(createOrderUseCase.createOrder(any(Order.class)))
                .thenReturn(Mono.error(new InvalidDataException("Error de validación")));

        Mono<ResponseEntity<ApiResponse<Order>>> result = orderController.create(orderDTO);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(400, response.getStatusCode().value());
                    assertNotNull(response.getBody());
                    assertEquals(400, response.getBody().getStatus());
                    assertEquals("Error de validación", response.getBody().getMessage());
                    assertNull(response.getBody().getData());
                })
                .verifyComplete();
    }

    // ===== Tests para getOrder =====

    @Test
    void getOrder_cuandoExiste_deberiaRetornarOrden() {
        when(findOrderByIdUsCase.findById(1)).thenReturn(Mono.just(order));

        Mono<ResponseEntity<ApiResponse<Order>>> result = orderController.getOrder(1);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertNotNull(response.getBody());
                    assertEquals(200, response.getBody().getStatus());
                    assertNotNull(response.getBody().getData());
                    assertEquals(1, response.getBody().getData().getOrderId());
                })
                .verifyComplete();

        verify(findOrderByIdUsCase, times(1)).findById(1);
    }

    @Test
    void getOrder_cuandoNoExiste_deberiaRetornar404() {
        when(findOrderByIdUsCase.findById(99)).thenReturn(Mono.empty());

        Mono<ResponseEntity<ApiResponse<Order>>> result = orderController.getOrder(99);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(404, response.getStatusCode().value());
                })
                .verifyComplete();

        verify(findOrderByIdUsCase, times(1)).findById(99);
    }

    // ===== Tests para confirmOrder =====

    @Test
    void confirmOrder_cuandoOrdenExisteYEstadoCREATED_deberiaConfirmar() {
        Order confirmedOrder = new Order(1, order.getCostumerId(), order.getProducts(),
                order.getTotalAmount(), OrderStatus.CONFIRMED);

        when(findOrderByIdUsCase.findById(1)).thenReturn(Mono.just(order));
        when(createOrderUseCase.createOrder(any(Order.class))).thenReturn(Mono.just(confirmedOrder));

        Mono<Order> result = orderController.confirmOrder(1);

        StepVerifier.create(result)
                .assertNext(o -> {
                    assertEquals(OrderStatus.CONFIRMED, o.getOrderStatus());
                })
                .verifyComplete();
    }

    @Test
    void confirmOrder_cuandoOrdenNoExiste_deberiaRetornarError() {
        when(findOrderByIdUsCase.findById(99)).thenReturn(Mono.empty());

        Mono<Order> result = orderController.confirmOrder(99);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof InvalidDataException)
                .verify();
    }

    // ===== Tests para cancelOrder =====

    @Test
    void cancelOrder_cuandoOrdenExisteYEstadoCREATED_deberiaCancelar() {
        Order cancelledOrder = new Order(1, order.getCostumerId(), order.getProducts(),
                order.getTotalAmount(), OrderStatus.CANCELLED);

        when(findOrderByIdUsCase.findById(1)).thenReturn(Mono.just(order));
        when(createOrderUseCase.createOrder(any(Order.class))).thenReturn(Mono.just(cancelledOrder));

        Mono<Order> result = orderController.cancelOrder(1);

        StepVerifier.create(result)
                .assertNext(o -> {
                    assertEquals(OrderStatus.CANCELLED, o.getOrderStatus());
                })
                .verifyComplete();
    }

    @Test
    void cancelOrder_cuandoOrdenNoExiste_deberiaRetornarError() {
        when(findOrderByIdUsCase.findById(99)).thenReturn(Mono.empty());

        Mono<Order> result = orderController.cancelOrder(99);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof InvalidDataException)
                .verify();
    }
}
