package com.example.shopease.controllers;

import com.example.shopease.dtos.requests.LoginRequest;
import com.example.shopease.dtos.requests.UserReqDTO;
import com.example.shopease.dtos.responses.UserRepDTO;
import com.example.shopease.entities.User;
import com.example.shopease.security.JwtUtil;
import com.example.shopease.security.UserPrincipal;
import com.example.shopease.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserReqDTO userReqDTO) {
        User user =  userService.registerUser(userReqDTO);
        String jwt = jwtUtil.generateToken(user.getEmail(), List.of(user.getRole().name()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserRepDTO(jwt,"User registered successfully",true));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        //This will take of the authentication and return exception if it fails
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new UserRepDTO(jwtUtil.generateToken(loginRequest.getEmail(), roles),"User Logged in successfully",true));
    }
}
