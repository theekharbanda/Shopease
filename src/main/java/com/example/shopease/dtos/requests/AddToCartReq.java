package com.example.shopease.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddToCartReq {

    @NotBlank(message = "Product id is required")
    private Long productId;

    @NotBlank(message = "Quantity is required")
    private int quantity;
}
