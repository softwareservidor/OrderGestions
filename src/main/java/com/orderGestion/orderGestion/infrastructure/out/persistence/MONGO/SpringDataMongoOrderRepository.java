package com.orderGestion.orderGestion.infrastructure.out.persistence.MONGO;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.orderGestion.orderGestion.infrastructure.entity.OrderEntity;

public interface SpringDataMongoOrderRepository extends MongoRepository<OrderEntity, Integer> {
}
