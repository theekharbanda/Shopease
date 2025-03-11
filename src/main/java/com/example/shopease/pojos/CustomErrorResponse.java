package com.example.shopease.pojos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomErrorResponse {
    private int status;
    private String message;
}
