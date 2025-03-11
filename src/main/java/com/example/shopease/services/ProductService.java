package com.example.shopease.services;

import com.example.shopease.entities.Product;
import com.example.shopease.repos.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Product createProduct(Product product) {
        if (product.getId() != null) {
            throw new IllegalArgumentException("New product cannot have an ID");
        }
        return productRepo.save(product);
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