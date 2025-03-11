package com.example.shopease.controllers;

import com.example.shopease.entities.Order;
import com.example.shopease.pojos.CustomErrorResponse;
import com.example.shopease.security.UserPrincipal;
import com.example.shopease.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            Order order = orderService.createOrder(userPrincipal.getId());
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to create order: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserOrders(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            List<Order> orders = orderService.getUserOrders(userPrincipal.getId());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to get orders: " + e.getMessage()));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderDetails(userPrincipal.getId(), orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to get order details: " + e.getMessage()));
        }
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable Long orderId) {
        try {
            Order order = orderService.cancelOrder(userPrincipal.getId(), orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to cancel order: " + e.getMessage()));
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                             @PathVariable Long orderId,
                                             @RequestParam Order.Status status) {
        try {
            Order order = orderService.updateOrderStatus(userPrincipal.getId(), orderId, status);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to update order status: " + e.getMessage()));
        }
    }
}
