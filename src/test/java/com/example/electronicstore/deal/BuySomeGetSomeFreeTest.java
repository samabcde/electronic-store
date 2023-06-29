package com.example.electronicstore.deal;

import com.example.electronicstore.basket.Basket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class BuySomeGetSomeFreeTest {

    public static final long MONITOR_ID = 1L;
    public static final long KEYBOARD_ID = 2L;

    @Test
    void Given_BasketIsEmpty_When_calculate_Then_ReturnEmptyFreeDeal() {
        BuySomeGetSomeFree buySomeGetSomeFree = new BuySomeGetSomeFree(Set.of(MONITOR_ID), List.of(KEYBOARD_ID));
        Basket empty = new Basket(1L, "customer1", Collections.emptyList());

        FreeDeal freeDeal = buySomeGetSomeFree.calculate(empty);
        Assertions.assertTrue(freeDeal.getProductIds().isEmpty());
    }
}