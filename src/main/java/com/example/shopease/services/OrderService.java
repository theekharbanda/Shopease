package com.example.shopease.services;

import com.example.shopease.entities.*;
import com.example.shopease.repos.OrderRepo;
import com.example.shopease.repos.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final CartService cartService;

    public OrderService(OrderRepo orderRepo, UserRepo userRepo, CartService cartService) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.cartService = cartService;
    }

    @Transactional
    public Order createOrder(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        final Order newOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(Order.Status.PENDING)
                .build();

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .order(newOrder)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getUnitPrice())
                        .build())
                .collect(Collectors.toList());

        newOrder.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(newOrder);

        // Clear the cart after creating order
        cartService.clearCart(userId);

        return savedOrder;
    }

    public List<Order> getUserOrders(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return orderRepo.findByUserOrderByOrderDateDesc(user);
    }

    public Order getOrderDetails(Long userId, Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Order does not belong to user");
        }

        return order;
    }

    @Transactional
    public Order cancelOrder(Long userId, Long orderId) {
        Order order = getOrderDetails(userId, orderId);

        if (order.getStatus() != Order.Status.PENDING) {
            throw new IllegalStateException("Can only cancel pending orders");
        }

        order.setStatus(Order.Status.CANCELLED);
        return orderRepo.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long userId, Long orderId, Order.Status status) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        if (!user.isAdmin()) {
            throw new IllegalStateException("Only admins can update order status");
        }

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() == Order.Status.CANCELLED) {
            throw new IllegalStateException("Cannot update status of cancelled order");
        }

        order.setStatus(status);
        return orderRepo.save(order);
    }

    public BigDecimal calculateOrderTotal(Order order) {
        return order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
