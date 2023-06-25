package com.example.electronicstore.basket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BasketItemResponse {
    private Long productId;
    private Long basketId;
    private Integer count;
}
