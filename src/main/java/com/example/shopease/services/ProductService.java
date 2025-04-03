package com.example.shopease.services;

import com.example.shopease.dtos.requests.AddProductReq;
import com.example.shopease.entities.Product;
import com.example.shopease.repos.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepo.findAll(pageable);
    }

    public Product getProduct(Long productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Transactional
    public Product createProduct(AddProductReq product) {
        if (productRepo.existsByName(product.getName())) {
            throw new IllegalArgumentException("Product name already exists");
        }
        Product newProduct = Product.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(BigDecimal.valueOf(product.getPrice()))
                .stock(product.getStock())
                .build();
        return productRepo.save(newProduct);
    }

    @Transactional
    public Product updateProduct(Long productId, Product product) {
        Product existingProduct = getProduct(productId);
        
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        
        return productRepo.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepo.existsById(productId)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepo.deleteById(productId);
    }
} 