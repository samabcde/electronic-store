package com.example.electronicstore.deal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.example.electronicstore.basket.Basket;
import com.example.electronicstore.basket.BasketItem;
import com.example.electronicstore.product.Product;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

class BuyXGetYOffAtNextTest {
    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true,
            textBlock = """
                       x,    y, price, count, expectedDiscount
                       1,    0,   100,     0,                0
                       1,   10,   100,     1,                0
                       0,   10,   100,     1,               10
                       1,   20,   100,     2,               20
                       1,   20,   100,     3,               20
                       1,   20,   100,     4,               40
                       4,   30,   100,    10,               60
                    """)
    void Given_basketItemForProductWithNoneDeal_When_calculate_Then_returnAppliedDealWithDiscount(Integer x, Integer y, BigDecimal price, Integer count, BigDecimal expectedDiscount) {
        Deal deal = new BuyXGetYOffAtNext(x, y);
        Product product = new Product(1L, "mouse", price, deal);
        BasketItem basketItem = new BasketItem(1L, new Basket(1L), product, count, 1L);
        AppliedDeal expected = new AppliedDeal(deal, 1L, "mouse", expectedDiscount);

        assertEquals(expected, deal.calculate(basketItem));
    }

}