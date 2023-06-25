package com.example.electronicstore.deal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.example.electronicstore.basket.Basket;
import com.example.electronicstore.basket.BasketItem;
import com.example.electronicstore.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class NoneTest {
    @Test
    void Given_basketItemForProductWithNoneDeal_When_calculate_Then_returnAppliedDealWith0Discount() {
        Deal deal = new None();
        Product product = new Product(1L, "mouse", new BigDecimal("7.5"), deal);
        BasketItem basketItem = new BasketItem(1L, new Basket(1L), product, 1, 1L);
        AppliedDeal expected = new AppliedDeal(deal, 1L, "mouse", BigDecimal.ZERO);

        assertEquals(expected, deal.calculate(basketItem));
    }

}