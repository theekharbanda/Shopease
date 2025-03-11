package com.example.shopease.repos;

import com.example.shopease.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepo extends JpaRepository<OrderItem, Long> {
}
