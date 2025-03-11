package com.example.shopease.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

//Custom Annotation
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.PARAMETER, ElementType.TYPE})
@AuthenticationPrincipal
public @interface CurrentUser {
}
