package com.example.shopease.controllers;

import com.example.shopease.dtos.requests.PaymentRequest;
import com.example.shopease.entities.Payment;
import com.example.shopease.pojos.CustomErrorResponse;
import com.example.shopease.security.UserPrincipal;
import com.example.shopease.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process/{orderId}")
    public ResponseEntity<?> processPayment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable Long orderId,
                                          @RequestBody PaymentRequest paymentRequest) {
        try {
            Payment payment = paymentService.processPayment(userPrincipal.getId(), orderId, paymentRequest);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Payment failed: " + e.getMessage()));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getPaymentStatus(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable Long orderId) {
        try {
            Payment payment = paymentService.getPaymentStatus(userPrincipal.getId(), orderId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to get payment status: " + e.getMessage()));
        }
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<?> refundPayment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @PathVariable Long orderId) {
        try {
            Payment payment = paymentService.refundPayment(userPrincipal.getId(), orderId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Refund failed: " + e.getMessage()));
        }
    }
}
