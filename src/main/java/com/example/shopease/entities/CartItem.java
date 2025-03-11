package com.example.shopease.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private BigDecimal unitPrice;

    public BigDecimal calculateTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
