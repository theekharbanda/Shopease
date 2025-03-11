package com.example.shopease.repos;

import com.example.shopease.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
}
