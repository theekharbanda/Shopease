package com.example.shopease.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@AllArgsConstructor@NoArgsConstructor
@Builder
@Setter
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy ="product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItem> cartItems;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
}
