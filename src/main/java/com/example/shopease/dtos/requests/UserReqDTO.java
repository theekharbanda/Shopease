package com.example.shopease.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReqDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid Email format")
    @NotBlank(message = "E-mail is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
