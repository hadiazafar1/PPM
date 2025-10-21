package com.example.codingexercise.gateway;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExchangeRateClient {

    private final RestTemplate restTemplate;
    private final String fxBaseUrl;

    public ExchangeRateClient(RestTemplate restTemplate,
            @Value("${fx.service.url}") String fxBaseUrl) {
        this.restTemplate = restTemplate;
        this.fxBaseUrl = fxBaseUrl; // e.g., https://api.frankfurter.app/latest
    }

    // Get exchange rate from USD to target currency
    public BigDecimal getExchangeRateFromUsdTo(String targetCurrency) {
        try {
            if (targetCurrency == null || targetCurrency.isEmpty() || targetCurrency.equalsIgnoreCase("USD")) {
                return BigDecimal.ONE;
            }
            var url = this.fxBaseUrl + "?amount=1&from=USD&to=" + targetCurrency.toUpperCase();
            var response = restTemplate.getForObject(url, Map.class);
            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
            Object values = rates.get(targetCurrency.toUpperCase());
            return values instanceof Number ? BigDecimal.valueOf(((Number) values).doubleValue()) : BigDecimal.ONE;
        } catch (Exception e) {
            // Handle exceptions (e.g., logging)
            return BigDecimal.ONE; // safe default
        }
    }

}
