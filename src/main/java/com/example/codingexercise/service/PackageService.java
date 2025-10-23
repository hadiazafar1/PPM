package com.example.codingexercise.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.codingexercise.dto.PackageResponse;
import com.example.codingexercise.dto.Product;
import com.example.codingexercise.gateway.ExchangeRateClient;
import com.example.codingexercise.gateway.ProductServiceGateway;
import com.example.codingexercise.model.ProductPackage;

@Service
public class PackageService {

    private final ProductServiceGateway productServiceGateway;
    private final ExchangeRateClient exchangeRateClient;
    private static final int MONEY_SCALE = 2;
    private static final RoundingMode RM = RoundingMode.HALF_UP;

    public PackageService(ProductServiceGateway productServiceGateway,
            ExchangeRateClient exchangeRateClient) {
        this.productServiceGateway = productServiceGateway;
        this.exchangeRateClient = exchangeRateClient;
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
