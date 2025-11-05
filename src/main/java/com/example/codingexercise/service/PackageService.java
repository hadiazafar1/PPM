package com.example.codingexercise.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.codingexercise.dto.PackageResponse;
import com.example.codingexercise.dto.Product;
import com.example.codingexercise.gateway.ExchangeRateClient;
import com.example.codingexercise.gateway.ProductServiceGateway;
import com.example.codingexercise.model.ProductPackage;
import com.example.codingexercise.repository.PackageRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PackageService {

    private final ProductServiceGateway productServiceGateway;
    private final ExchangeRateClient exchangeRateClient;
    private static final int MONEY_SCALE = 2;
    private static final RoundingMode RM = RoundingMode.HALF_UP;
    private final PackageRepository packageRepository;

    public PackageService(ProductServiceGateway productServiceGateway,
            ExchangeRateClient exchangeRateClient, PackageRepository packageRepository) {
        this.productServiceGateway = productServiceGateway;
        this.exchangeRateClient = exchangeRateClient;
        this.packageRepository = packageRepository; 
    }

     @Transactional(readOnly = true)
     public List<PackageResponse> listPackages(String currency)
     {
        return packageRepository.findAll().stream()
            .map(pkg -> createResponse(pkg, currency))
            .toList();
     }

     @Transactional(readOnly = true)
     public PackageResponse getPackageById(String id, String currency) {

        var pkg = packageRepository.getPackage(id);
        return createResponse(pkg, currency);
     }

     @Transactional
    public PackageResponse createPackage(ProductPackage body, String currency) {
        var created = packageRepository.createPackage(
                body.getName(),
                body.getDescription(),
                body.getProductIds());
        return createResponse(created, currency);
    }

    @Transactional
    public PackageResponse updatePackage(String id, ProductPackage body, String currency) {
        var updated = packageRepository.update(
                id,
                body.getName(),
                body.getDescription(),
                body.getProductIds());
        return createResponse(updated, currency);
    }

    @Transactional
    public void deletePackage(String id) {
        packageRepository.delete(id);
    }

    // Create package response with converted price
    public PackageResponse createResponse(ProductPackage productPackage, String targetCurrency) {
        // Calculate total price in USD
        BigDecimal totalUsdPrice = productPackage.getProductIds().stream()
                .map(id -> {
                    var p = productServiceGateway.getProductById(id);
                    return p != null ? BigDecimal.valueOf(p.usdPrice()) : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Convert price to target currency
        BigDecimal exchangeRate = exchangeRateClient.getExchangeRateFromUsdTo(targetCurrency);
        // Calculate converted price
        BigDecimal convertedPrice = totalUsdPrice.multiply(exchangeRate).setScale(MONEY_SCALE, RM);

        String currency = (targetCurrency == null || targetCurrency.isEmpty()) ? "USD" : targetCurrency.toUpperCase();
        return new PackageResponse(
                productPackage.getId(),
                productPackage.getName(),
                convertedPrice,
                (productPackage.getProductIds() == null ? List.of() : productPackage.getProductIds()),
                productPackage.getDescription(),
                currency);

    }

}
