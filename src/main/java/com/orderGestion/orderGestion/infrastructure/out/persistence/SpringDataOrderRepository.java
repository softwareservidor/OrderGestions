package com.orderGestion.orderGestion.infrastructure.out.persistence;

import com.orderGestion.orderGestion.infrastructure.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, Integer> {
}
