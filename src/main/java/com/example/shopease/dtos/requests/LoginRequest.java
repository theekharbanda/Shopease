package com.example.shopease.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = "Invalid Email format")
    @NotBlank(message = "E-mail is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
