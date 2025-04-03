package com.example.shopease.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddProductReq {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotBlank(message = "Product price is required")
    private double price;

    @NotBlank(message = "Product stock is required")
    private int stock;
}
