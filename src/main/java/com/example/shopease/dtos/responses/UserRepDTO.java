package com.example.shopease.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserRepDTO {
    private String token;
    private String message;
    private boolean success;
}
