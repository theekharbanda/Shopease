package com.example.shopease.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    List<OrderItem> orderItems;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING,SHIPPED,DELIVERED,CANCELLED
    }

    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private Payment payment;

}
