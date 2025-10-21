package com.example.codingexercise.controller;

import com.example.codingexercise.dto.PackageResponse;
import com.example.codingexercise.model.ProductPackage;
import com.example.codingexercise.repository.PackageRepository;
import com.example.codingexercise.service.PackageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/packages")
public class PackageController {

    private final PackageRepository packageRepository;
    private final PackageService packageService;

    public PackageController(PackageRepository packageRepository, PackageService packageService) {
        this.packageRepository = packageRepository;
        this.packageService = packageService;
    }

    // Create a new package
    @Operation(summary = "Create a new package", description = "Creates a new product package with the provided details.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PackageResponse.class)))
    @PostMapping
    public PackageResponse createPackage(@RequestBody ProductPackage productPackage,
            @RequestParam(required = false) String currency) {

        var createdRecord = packageRepository.createPackage(productPackage.getName(), productPackage.getDescription(),
                productPackage.getProductIds());
        return packageService.createResponse(createdRecord, currency);
    }

    // Get a package by ID
    @Operation(summary = "Get a package by ID", description = "Retrieves the details of a specific product package.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PackageResponse.class)))
    @GetMapping("/{id}")
    public PackageResponse getPackage(@PathVariable String id, @RequestParam(required = false) String currency) {
        var record = packageRepository.getPackage(id);
        return packageService.createResponse(record, currency);
    }

    // Update a package by ID
    @Operation(summary = "Update a package by ID", description = "Updates the details of an existing product package.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PackageResponse.class)))
    @PutMapping("/{id}")
    public PackageResponse updatePackage(@PathVariable String id, @RequestBody ProductPackage updatedBody,
            @RequestParam(required = false) String currency) {
        var updatedRecord = packageRepository.update(id, updatedBody.getName(), updatedBody.getDescription(),
                updatedBody.getProductIds());
        return packageService.createResponse(updatedRecord, currency);
    }

    // Delete a package by ID
    @Operation(summary = "Delete a package by ID", description = "Deletes a specific product package.")
    @ApiResponse(responseCode = "204", description = "No Content")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePackage(@PathVariable String id) {
        packageRepository.delete(id);
    }

    // List all packages
    @Operation(summary = "List all packages", description = "Retrieves a list of all product packages.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PackageResponse.class)))
    @GetMapping
    public List<PackageResponse> getAllPackages(@RequestParam(required = false) String currency) {
        var packages = packageRepository.findAll();
        return packages.stream()
                .map(pkg -> packageService.createResponse(pkg, currency))
                .toList();
    }

}
