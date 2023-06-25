package com.example.electronicstore.basket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basket")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @Operation(summary = "Find a basket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found basket"),
            @ApiResponse(responseCode = "404", description = "Basket not found")})
    @GetMapping(path = "/{id}")
    public Basket getBasket(@PathVariable Long id) {
        return basketService.getBasket(id);
    }

    @Operation(summary = "Create a basket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created a basket"),
            @ApiResponse(responseCode = "400", description = "Basket is not valid")})
    @PostMapping(path = "")
    public Basket createBasket(@Valid @RequestBody Basket basket) {
        return basketService.createBasket(basket);
    }

    @Operation(summary = "Add an item to the basket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added an item"),
            @ApiResponse(responseCode = "400", description = "Request is not valid")})
    @PostMapping(path = "/basket-item")
    public BasketItemResponse addBasketItem(@Valid @RequestBody BasketItemRequest basketItemRequest) {
        BasketItem basketItem = basketService.addItem(basketItemRequest.getBasketId(), basketItemRequest.getProductId());
        return new BasketItemResponse(basketItem.getProduct().getId(), basketItem.getBasket().getId(), basketItem.getCount());
    }

    @Operation(summary = "Remove an item from the basket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Removed an item"),
            @ApiResponse(responseCode = "400", description = "Request is not valid"),
            @ApiResponse(responseCode = "404", description = "Item is not found"),
    })
    @DeleteMapping(path = "/basket-item")
    public BasketItemResponse removeBasketItem(@Valid @RequestBody BasketItemRequest basketItemRequest) {
        BasketItem basketItem = basketService.removeItem(basketItemRequest.getBasketId(), basketItemRequest.getProductId());
        return new BasketItemResponse(basketItem.getProduct().getId(), basketItem.getBasket().getId(), basketItem.getCount());
    }
}
