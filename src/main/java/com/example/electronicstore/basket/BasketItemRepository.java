package com.example.electronicstore.basket;

import com.example.electronicstore.product.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketItemRepository extends CrudRepository<BasketItem, Long> {
    Optional<BasketItem> findByBasketAndProduct(Basket basket, Product product);
}
