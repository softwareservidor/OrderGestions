package com.orderGestion.orderGestion.infrastructure;

import com.orderGestion.orderGestion.application.dto.OrderDTO;
import com.orderGestion.orderGestion.application.usecase.CreateOrderUseCase;
import com.orderGestion.orderGestion.application.usecase.FindOrderByIdUsCase;
import com.orderGestion.orderGestion.domain.model.Costumer;
import com.orderGestion.orderGestion.domain.model.Order;
import com.orderGestion.orderGestion.domain.model.Product;
import com.orderGestion.orderGestion.infrastructure.exception.InvalidDataException;
import com.orderGestion.orderGestion.infrastructure.message.OrdersMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/buyOrders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;

    private final FindOrderByIdUsCase findOrderByIdUsCase;

    public OrderController(CreateOrderUseCase createOrderUseCase, FindOrderByIdUsCase findOrderByIdUsCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.findOrderByIdUsCase = findOrderByIdUsCase;

    }


    @PostMapping("/createOrder")
    public Mono<ResponseEntity<ApiResponse<Order>>> create(@RequestBody final OrderDTO dto) {

        Order order = new Order(
                dto.getOrderId(),
                new Costumer(dto.getCostumerId().getCostumerId(), dto.getCostumerId().getName(), dto.getCostumerId().getEmail()),
                dto.getProducts().stream().map(product -> new Product(product.getProductId(), product.getName(), product.getPrice())).toList(),
                dto.getTotalAmount(),
                dto.getOrderStatus()
        );
        return createOrderUseCase.createOrder(order)
                .map(saved -> ResponseEntity.ok(new ApiResponse<>(saved, 201, OrdersMessages.ORDER_CREATED)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest()
                        .body(new ApiResponse<Order>(null, 400, e.getMessage()))));

    }

    @GetMapping("/getById/{id}")
    public Mono<ResponseEntity<ApiResponse<Order>>> getReservation(@PathVariable int id) {
        return findOrderByIdUsCase.findById(id)
                .map(order -> ResponseEntity.ok(new ApiResponse<>(order, 200, OrdersMessages.ORDER_FOUND)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/confirm")
    public Mono<Order> confirmOrder(@PathVariable int id) {
        return getReservation(id)
                .flatMap(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        Order order = response.getBody().getData();
                        order.setOrderStatus("CONFIRMED");
                        return createOrderUseCase.createOrder(order);
                    } else {
                        return Mono.error(new InvalidDataException(OrdersMessages.ORDER_NOT_FOUND));
                    }
                });
    }

    @PatchMapping("/{id}/cancel")
    public Mono<Order> cancelOrder(@PathVariable int id) {
        return getReservation(id)
                .flatMap(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        Order order = response.getBody().getData();
                        order.setOrderStatus("CANCELLED");
                        return createOrderUseCase.createOrder(order);
                    } else {
                        return Mono.error(new InvalidDataException(OrdersMessages.ORDER_NOT_FOUND));
                    }
                });
    }
}