package com.example.shopease.services;

import com.example.shopease.entities.Cart;
import com.example.shopease.entities.CartItem;
import com.example.shopease.entities.Product;
import com.example.shopease.entities.User;
import com.example.shopease.repos.CartRepo;
import com.example.shopease.repos.CartItemsRepo;
import com.example.shopease.repos.ProductRepo;
import com.example.shopease.repos.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {
    private final CartRepo cartRepo;
    private final CartItemsRepo cartItemsRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    public CartService(CartRepo cartRepo, CartItemsRepo cartItemsRepo, UserRepo userRepo, ProductRepo productRepo) {
        this.cartRepo = cartRepo;
        this.cartItemsRepo = cartItemsRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Transactional
    public Cart addToCart(Long userId, CartItem cartItem) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Cart cart = user.getCart();
        if (cart == null) {
            cart = Cart.builder()
                    .user(user)
                    .build();
            cart = cartRepo.save(cart);
            user.setCart(cart);
        }

        Product product = productRepo.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setUnitPrice(product.getPrice());
        
        cart.getItems().add(cartItem);
        return cartRepo.save(cart);
    }

    public List<CartItem> getCartItems(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Cart cart = user.getCart();
        if (cart == null) {
            return List.of();
        }
        return cart.getItems();
    }

    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Cart cart = user.getCart();
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            cartRepo.save(cart);
        }
    }

    @Transactional
    public void clearCart(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Cart cart = user.getCart();
        if (cart != null) {
            cart.getItems().clear();
            cartRepo.save(cart);
        }
    }

    @Transactional
    public Cart updateCartItemQuantity(Long userId, Long productId, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Cart cart = user.getCart();
        if (cart == null) {
            throw new EntityNotFoundException("Cart not found");
        }

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product not found in cart"));

        cartItem.setQuantity(quantity);
        return cartRepo.save(cart);
    }
}
