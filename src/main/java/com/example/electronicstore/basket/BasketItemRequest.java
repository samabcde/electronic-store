package com.example.electronicstore.basket;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BasketItemRequest {
    @NotNull
    private Long productId;
    @NotNull
    private Long basketId;
}
