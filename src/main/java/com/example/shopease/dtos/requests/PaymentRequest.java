package com.example.shopease.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String paymentMethod; // CARD, UPI, etc.
    private String paymentToken;  // Payment gateway token
    private String currency;      // USD, EUR, etc.
} 