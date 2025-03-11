package com.example.shopease.services;

import com.example.shopease.dtos.requests.PaymentRequest;
import com.example.shopease.entities.Order;
import com.example.shopease.entities.Payment;
import com.example.shopease.entities.User;
import com.example.shopease.repos.OrderRepo;
import com.example.shopease.repos.PaymentRepo;
import com.example.shopease.repos.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final OrderService orderService;

    public PaymentService(PaymentRepo paymentRepo, OrderRepo orderRepo, UserRepo userRepo, OrderService orderService) {
        this.paymentRepo = paymentRepo;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.orderService = orderService;
    }

    @Transactional
    public Payment processPayment(Long userId, Long orderId, PaymentRequest paymentRequest) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Order does not belong to user");
        }

        if (order.getPayment() != null) {
            throw new IllegalStateException("Payment already exists for this order");
        }

        BigDecimal amount = orderService.calculateOrderTotal(order);

        // Here you would integrate with a payment gateway
        // For now, we'll simulate a successful payment
        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
                .status(Payment.Status.SUCCESS)
                .build();

        payment = paymentRepo.save(payment);

        // Update order status
        order.setStatus(Order.Status.SHIPPED);
        orderRepo.save(order);

        return payment;
    }

    public Payment getPaymentStatus(Long userId, Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Order does not belong to user");
        }

        if (order.getPayment() == null) {
            throw new EntityNotFoundException("No payment found for this order");
        }

        return order.getPayment();
    }

    @Transactional
    public Payment refundPayment(Long userId, Long orderId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Order does not belong to user");
        }

        Payment payment = order.getPayment();
        if (payment == null) {
            throw new EntityNotFoundException("No payment found for this order");
        }

        if (payment.getStatus() != Payment.Status.SUCCESS) {
            throw new IllegalStateException("Can only refund successful payments");
        }

        // Here you would integrate with payment gateway for refund
        // For now, we'll simulate a successful refund
        payment.setStatus(Payment.Status.REFUNDED);
        payment = paymentRepo.save(payment);

        // Update order status
        order.setStatus(Order.Status.CANCELLED);
        orderRepo.save(order);

        return payment;
    }
}
