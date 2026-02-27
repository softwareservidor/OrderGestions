package com.orderGestion.orderGestion.infrastructure.out.persistence.JPA;

import com.orderGestion.orderGestion.infrastructure.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJPAOrderRepository extends JpaRepository<OrderEntity, Integer> {
}
