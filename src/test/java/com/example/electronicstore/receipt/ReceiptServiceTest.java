package com.example.electronicstore.receipt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.example.electronicstore.basket.Basket;
import com.example.electronicstore.basket.BasketItem;
import com.example.electronicstore.basket.BasketNotFoundException;
import com.example.electronicstore.basket.BasketService;
import com.example.electronicstore.deal.AppliedDeal;
import com.example.electronicstore.deal.BuyXGetYOffAtNext;
import com.example.electronicstore.deal.None;
import com.example.electronicstore.product.Product;
import com.example.electronicstore.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {
    @Mock
    BasketService basketService;
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ReceiptService receiptService;

    @Test
    void Given_basketNotFound_When_calculateReceipt_Then_throwBasketNotFoundException() {
        when(basketService.getBasket(1L)).thenThrow(BasketNotFoundException.class);

        assertThrows(BasketNotFoundException.class, () -> receiptService.calculate(new ReceiptRequest(1L)));

        verify(basketService).getBasket(1L);
    }


    @Test
    void Given_basketIsFound_When_calculateReceipt_Then_returnCalculatedReceipt() {
        Product mouse = new Product(1L, "mouse", new BigDecimal("8.5"), new None());
        Product keyboard = new Product(2L, "keyboard", new BigDecimal("10.5"), new BuyXGetYOffAtNext(2, 20));
        when(basketService.getBasket(1L)).thenReturn(new Basket(
                1L,
                "customer1",
                List.of(
                        new BasketItem(1L, new Basket(1L), mouse, 2, 1L),
                        new BasketItem(2L, new Basket(1L), keyboard, 4, 1L)
                )
        ));

        Receipt actual = receiptService.calculate(new ReceiptRequest(1L));

        verify(basketService).getBasket(1L);
        assertEquals(new Receipt(1L, "customer1",
                List.of(
                        new Purchase(1L, "mouse", new BigDecimal("8.5"), 2),
                        new Purchase(2L, "keyboard", new BigDecimal("10.5"), 4)
                ),
                List.of(
                        new AppliedDeal(new BuyXGetYOffAtNext(2, 20), 2L, "keyboard", new BigDecimal("2.1"))
                ),
                new BigDecimal("56.9")
        ), actual);
    }

    @Test
    void Given_basketItemFullfillFreeDeal_When_calculateReceipt_Then_returnCalculatedReceiptWithFreeItem() {
        Product mouse = new Product(3L, "mouse", new BigDecimal("8.5"), new None());
        Product keyboard = new Product(4L, "keyboard", new BigDecimal("10.5"), new BuyXGetYOffAtNext(2, 20));
        when(productRepository.findById(4L)).thenReturn(Optional.of(keyboard));
        when(basketService.getBasket(1L)).thenReturn(new Basket(
                1L,
                "customer1",
                List.of(
                        new BasketItem(3L, new Basket(1L), mouse, 2, 1L)
                )
        ));

        Receipt actual = receiptService.calculate(new ReceiptRequest(1L));

        verify(basketService).getBasket(1L);
        assertEquals(new Receipt(1L, "customer1",
                List.of(
                        new Purchase(3L, "mouse", new BigDecimal("8.5"), 2),
                        new Purchase(4L, "keyboard", BigDecimal.ZERO, 1)
                ),
                List.of(
//                        new AppliedDeal(new BuyXGetYOffAtNext(2, 20), 2L, "keyboard", new BigDecimal("2.1"))
                ),
                new BigDecimal("17.0")
        ), actual);
    }
}