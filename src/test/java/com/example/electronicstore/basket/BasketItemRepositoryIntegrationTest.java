package com.example.electronicstore.basket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.example.electronicstore.deal.None;
import com.example.electronicstore.product.Product;
import com.example.electronicstore.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class BasketItemRepositoryIntegrationTest {
    @Autowired
    BasketItemRepository basketItemRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BasketService basketService;

    @Test
    void concurrentUpdateOnBasketItem_guardedByOptimisticLock() throws InterruptedException {
        Product product = productRepository.save(new Product(null, "mouse", new BigDecimal("10"), new None()));
        Basket basket = basketRepository.save(new Basket(null, "someone", Collections.emptyList()));
        BasketItem basketItem = basketItemRepository.save(
                new BasketItem(null,
                        basket,
                        product,
                        0, null));
        final Long basketItemId = basketItem.getId();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger optimisticLockFailureCount = new AtomicInteger();
        int concurrentUpdateCount = 50;
        for (int i = 0; i < concurrentUpdateCount; i++) {
            executor.submit(
                    () -> {
                        try {
                            basketService.addItem(basket.getId(), product.getId());
                        } catch (OptimisticLockingFailureException ex) {
                            optimisticLockFailureCount.incrementAndGet();
                        }
                    }
            );
        }
        executor.shutdown();
        executor.awaitTermination(10L, TimeUnit.SECONDS);
        int updatedCount = basketItemRepository.findById(basketItemId).get().getCount();
        assertEquals(concurrentUpdateCount, updatedCount + optimisticLockFailureCount.get());
    }

}