package com.example.electronicstore.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Create a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created a product"),
            @ApiResponse(responseCode = "400", description = "Product is not valid")})
    @PostMapping(path = "/product")
    public Product create(@Valid @RequestBody Product product) {
        return adminService.createProduct(product);
    }

    @Operation(summary = "Find a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found product"),
            @ApiResponse(responseCode = "404", description = "Product not found")})
    @GetMapping(path = "/product/{id}")
    public Product get(@PathVariable Long id) throws ProductNotFoundException {
        return adminService.getProduct(id);
    }

    @Operation(summary = "Update a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated product"),
            @ApiResponse(responseCode = "404", description = "Product not found")})
    @PutMapping(path = "/product/{id}")
    public Product update(@Valid @RequestBody Product product, @PathVariable Long id) throws ProductNotFoundException {
        return adminService.updateProduct(product, id);
    }

    @Operation(summary = "Update a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated product")})
    @DeleteMapping(path = "/product/{id}")
    void delete(@PathVariable Long id) {
        adminService.deleteProduct(id);
    }
}
