package com.example.shopease.config;

import com.example.shopease.pojos.CustomErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.warn("User doesn't have access to perform the operation", accessDeniedException);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .status(2)
                .message(accessDeniedException.getMessage())
                .build();

        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(errorResponse));
    }
}
