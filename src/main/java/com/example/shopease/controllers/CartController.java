package com.example.shopease.controllers;

import com.example.shopease.entities.Cart;
import com.example.shopease.entities.CartItem;
import com.example.shopease.entities.User;
import com.example.shopease.pojos.CustomErrorResponse;
import com.example.shopease.security.UserPrincipal;
import com.example.shopease.services.CartService;
import com.example.shopease.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @RequestBody CartItem cartItem) {
        try {
            User user = userService.getUserById(userPrincipal.getId());
            Cart cart = cartService.addToCart(user.getId(), cartItem);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to add item to cart: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getCartItems(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            List<CartItem> cartItems = cartService.getCartItems(userPrincipal.getId());
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to get cart items: " + e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable Long productId) {
        try {
            cartService.removeFromCart(userPrincipal.getId(), productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to remove item from cart: " + e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            cartService.clearCart(userPrincipal.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to clear cart: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateCartItemQuantity(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @PathVariable Long productId,
                                                  @RequestParam int quantity) {
        try {
            Cart updatedCart = cartService.updateCartItemQuantity(userPrincipal.getId(), productId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomErrorResponse(400, "Failed to update cart item quantity: " + e.getMessage()));
        }
    }
}
