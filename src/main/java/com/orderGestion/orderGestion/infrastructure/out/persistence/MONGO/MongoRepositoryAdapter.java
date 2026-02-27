package com.orderGestion.orderGestion.infrastructure.out.persistence.MONGO;

import com.orderGestion.orderGestion.application.port.OrderRepository;
import com.orderGestion.orderGestion.domain.model.Costumer;
import com.orderGestion.orderGestion.domain.model.Order;
import com.orderGestion.orderGestion.domain.model.Product;
import com.orderGestion.orderGestion.infrastructure.entity.CostumerEntity;
import com.orderGestion.orderGestion.infrastructure.entity.OrderEntity;
import com.orderGestion.orderGestion.infrastructure.entity.ProductEntity;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;

public class MongoRepositoryAdapter implements OrderRepository {

    private final SpringDataMongoOrderRepository springDataMongoOrderRepository;

    public MongoRepositoryAdapter(SpringDataMongoOrderRepository springDataMongoOrderRepository) {
        this.springDataMongoOrderRepository = springDataMongoOrderRepository;
    }

    @Override
    public Mono<Order> save(Order order) {
        return Mono.fromCallable(() ->{
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderId(order.getOrderId());

            CostumerEntity costumerEntity = new CostumerEntity();
            costumerEntity.setCostumerId(order.getCostumerId().getCostumerId());
            costumerEntity.setName(order.getCostumerId().getName());
            costumerEntity.setEmail(order.getCostumerId().getEmail());
            orderEntity.setCostumerId(costumerEntity);

            List<ProductEntity> productEntities = order.getProducts().stream().map(product -> {
                ProductEntity productEntity = new ProductEntity();
                productEntity.setProductId(product.getProductId());
                productEntity.setName(product.getName());
                productEntity.setPrice(product.getPrice());
                return productEntity;
            }).toList();

            orderEntity.setProducts(productEntities);
            orderEntity.setTotalAmount(order.getTotalAmount());
            orderEntity.setOrderStatus(order.getOrderStatus());

            return springDataMongoOrderRepository.save(orderEntity);
        }).map(savedEntity -> {
            return order;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Order> findById(int id) {
        return Mono.fromCallable(() -> springDataMongoOrderRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap((Optional<OrderEntity> optionalEntity) -> {
                    if (optionalEntity.isPresent()) {
                        OrderEntity orderEntity = optionalEntity.get();
                        Order order = new Order();
                        order.setOrderId(orderEntity.getOrderId());

                        CostumerEntity costumerEntity = orderEntity.getCostumerId();
                        order.setCostumerId(new Costumer(costumerEntity.getCostumerId(), costumerEntity.getName(), costumerEntity.getEmail()));

                        List<Product> products = orderEntity.getProducts().stream().map(productEntity -> {
                            return new Product(productEntity.getProductId(), productEntity.getName(), productEntity.getPrice());
                        }).toList();

                        order.setProducts(products);
                        order.setTotalAmount(orderEntity.getTotalAmount());
                        order.setOrderStatus(orderEntity.getOrderStatus());

                        return Mono.just(order);
                    } else {
                        return Mono.empty();
                    }
                });
    }
}
