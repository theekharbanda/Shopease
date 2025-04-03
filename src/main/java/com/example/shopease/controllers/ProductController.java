package com.example.shopease.controllers;

import com.example.shopease.dtos.requests.AddProductReq;
import com.example.shopease.entities.Product;
import com.example.shopease.entities.User;
import com.example.shopease.pojos.CustomErrorResponse;
import com.example.shopease.security.CurrentUser;
import com.example.shopease.security.UserPrincipal;
import com.example.shopease.services.ProductService;
import com.example.shopease.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(Pageable pageable) {
        try {
            Page<Product> products = productService.getAllProducts(pageable);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to get products: " + e.getMessage()));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        try {
            Product product = productService.getProduct(productId);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to get product: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@CurrentUser UserPrincipal userPrincipal,
                                         @RequestBody AddProductReq addProduct) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserById(userPrincipal.getId());
            if (!user.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new CustomErrorResponse(403, "Only admins can create products"));
            }
            Product createdProduct = productService.createProduct(addProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to create product: " + e.getMessage()));
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @PathVariable Long productId,
                                         @RequestBody Product product) {
        try {
            User user = userService.getUserById(userPrincipal.getId());
            if (!user.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new CustomErrorResponse(403, "Only admins can update products"));
            }
            Product updatedProduct = productService.updateProduct(productId, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to update product: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @PathVariable Long productId) {
        try {
            User user = userService.getUserById(userPrincipal.getId());
            if (!user.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new CustomErrorResponse(403, "Only admins can delete products"));
            }
            productService.deleteProduct(productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to delete product: " + e.getMessage()));
        }
    }
} 