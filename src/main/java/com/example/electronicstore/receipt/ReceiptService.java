package com.example.electronicstore.receipt;

import com.example.electronicstore.basket.Basket;
import com.example.electronicstore.basket.BasketService;
import com.example.electronicstore.deal.AppliedDeal;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@Service
public class ReceiptService {
    private final BasketService basketService;

    public ReceiptService(BasketService basketService) {
        this.basketService = basketService;
    }

    @Transactional
    public Receipt calculate(ReceiptRequest request) {
        Basket basket = basketService.getBasket(request.getBasketId());
        BigDecimal totalOriginalPrice = basket.getBasketItems().stream().map((item) -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getCount()), MathContext.DECIMAL64))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<Purchase> purchases = basket.getBasketItems().stream()
                .map((item) -> new Purchase(item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getPrice(), item.getCount()))
                .toList();
        List<AppliedDeal> appliedDeals = basket.getBasketItems().stream()
                .map((item) -> item.getProduct().getDeal().calculate(item))
                .filter(AppliedDeal::isNotNoneDeal)
                .toList();
        BigDecimal totalDiscount = appliedDeals.stream().map(AppliedDeal::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Receipt(basket.getId(), basket.getCustomerId(), purchases, appliedDeals, totalOriginalPrice.subtract(totalDiscount));
    }
}
