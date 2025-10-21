package com.example.codingexercise.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

// DTO for package response
public record PackageResponse(String id, String name, BigDecimal price,
@JsonProperty("productIds") List<String> productIds,
 String description, String currency) {
}
