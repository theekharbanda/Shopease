package com.example.shopease.controllers;

import com.example.shopease.dtos.responses.UserRepDTO;
import com.example.shopease.dtos.requests.UserReqDTO;
import com.example.shopease.security.CustomUserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final CustomUserDetailsService userDetailsService;

    public UserController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


}
