package com.example.shopease.repos;

import com.example.shopease.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepo extends JpaRepository<CartItem, Long> {
}
