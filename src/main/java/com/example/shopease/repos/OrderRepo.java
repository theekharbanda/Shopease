package com.example.shopease.repos;

import com.example.shopease.entities.Order;
import com.example.shopease.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
}
