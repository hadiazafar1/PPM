package com.example.codingexercise.gateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.codingexercise.dto.Product;

@Component
public class ProductServiceGateway {

    private final RestTemplate restTemplate;
    private final String baseUrl; // from properties
    public ProductServiceGateway(RestTemplate restTemplate,
            @Value("${product.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        // Ensure it ends with a slash once
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    // Get a product by ID
    public Product getProductById(String productId) {
        try {
            var p = restTemplate.getForObject(this.baseUrl + "products/{id}", Product.class, productId);
            if (p == null) {
                return new Product(productId, "N/A", 0); // safe default
            }
            return p;
        } catch (Exception e) {
            // Handle exceptions (e.g., logging)
            return new Product(productId, "N/A", 0); // safe default
        }
    }

    // Get all products
    public List<Product> getAllProducts() {
        try {
            var response = restTemplate.getForObject(this.baseUrl + "products", Product[].class);
            return List.of(response != null ? response : new Product[0]);
        } catch (Exception e) {
            // Handle exceptions (e.g., logging)
            return List.of();
        }
    }
}
