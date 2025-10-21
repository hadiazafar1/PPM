package com.example.codingexercise.repository;

import com.example.codingexercise.dto.Product;
import com.example.codingexercise.model.ProductPackage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class PackageRepository {

    // In-memory store for packages
    private final Map<String, ProductPackage> productPackageStore = new LinkedHashMap<>();

    // Create a new package
    public ProductPackage createPackage(String name, String description, List<String> productIds) {
        ProductPackage newProductPackage = new ProductPackage(UUID.randomUUID().toString(), name, description,
                productIds);
        productPackageStore.put(newProductPackage.getId(), newProductPackage);
        return newProductPackage;
    }

    // Get a package by ID
    public ProductPackage getPackage(String id) {
        ProductPackage productPackage = productPackageStore.get(id);
        if (productPackage == null) {
            throw new IllegalArgumentException("Package with id " + id + " not found");
        }
        return productPackage;
    }

    // Update a package by ID
    public ProductPackage update(String id, String name, String desc, List<String> productIds) {
        ProductPackage existing = getPackage(id);
        existing.setName(name);
        existing.setDescription(desc);
        existing.setProductIds(productIds);
        return existing;
    }

    // Delete a package by ID
    public void delete(String id) {
        productPackageStore.remove(id);
    }

    // List all packages
    public List<ProductPackage> findAll() {
        return new ArrayList<>(productPackageStore.values());
    }
}
