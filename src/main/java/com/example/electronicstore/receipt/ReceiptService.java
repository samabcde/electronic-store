package com.example.electronicstore.receipt;

import com.example.electronicstore.basket.Basket;
import com.example.electronicstore.basket.BasketService;
import com.example.electronicstore.deal.AppliedDeal;
import com.example.electronicstore.deal.BuySomeGetSomeFree;
import com.example.electronicstore.deal.FreeDeal;
import com.example.electronicstore.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ReceiptService {
    private final BasketService basketService;
    private final ProductRepository productRepository;

    public ReceiptService(BasketService basketService,
                          ProductRepository productRepository) {
        this.basketService = basketService;
        this.productRepository = productRepository;
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
        BuySomeGetSomeFree buySomeGetSomeFree = new BuySomeGetSomeFree(Set.of(3L), List.of(4L));
        FreeDeal freeDeal = buySomeGetSomeFree.calculate(basket);
        List<Purchase> freePurchases = freeDeal.getProductIds().stream().map(id -> productRepository.findById(id)).map(product ->
                new Purchase(product.get().getId(), product.get().getName(), BigDecimal.ZERO, 1)
        ).toList();
        List<Purchase> allPurchases = new ArrayList<>();
        allPurchases.addAll(purchases);
        allPurchases.addAll(freePurchases);
        // add Free item to purchase
        BigDecimal totalDiscount = appliedDeals.stream().map(AppliedDeal::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Receipt(basket.getId(), basket.getCustomerId(), allPurchases, appliedDeals, totalOriginalPrice.subtract(totalDiscount));
    }
}
