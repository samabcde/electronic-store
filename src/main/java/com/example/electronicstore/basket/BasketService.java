package com.example.electronicstore.basket;

import com.example.electronicstore.product.Product;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BasketService {
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;

    public BasketService(BasketRepository basketRepository, BasketItemRepository basketItemRepository) {
        this.basketRepository = basketRepository;
        this.basketItemRepository = basketItemRepository;
    }

    @Transactional
    public Basket getBasket(Long id) {
        return basketRepository.findById(id).orElseThrow(BasketNotFoundException::new);
    }

    @Transactional
    public Basket createBasket(Basket basket) {
        return basketRepository.save(basket);
    }

    @Transactional
    public BasketItem addItem(Long basketId, Long productId) {
        Basket basket = new Basket(basketId);
        Product product = new Product(productId);
        Optional<BasketItem> item = basketItemRepository.findByBasketAndProduct(basket, product);
        return item.map(this::incrementCount).orElseGet(() -> createItem(basket, product));
    }

    @Transactional
    public BasketItem removeItem(Long basketId, Long productId) {
        Basket basket = new Basket(basketId);
        Product product = new Product(productId);
        Optional<BasketItem> item = basketItemRepository.findByBasketAndProduct(basket, product);
        return item.map(this::decrementCount).orElseThrow(BasketItemNotFoundException::new);
    }

    private BasketItem createItem(Basket basket, Product product) {
        BasketItem basketItem = new BasketItem();
        basketItem.setBasket(basket);
        basketItem.setProduct(product);
        basketItem.setCount(1);
        return basketItemRepository.save(basketItem);
    }

    private BasketItem incrementCount(BasketItem basketItem) {
        basketItem.setCount(basketItem.getCount() + 1);
        return basketItemRepository.save(basketItem);
    }

    private BasketItem decrementCount(BasketItem basketItem) {
        if (basketItem.getCount() == 0) {
            return basketItem;
        }
        basketItem.setCount(basketItem.getCount() - 1);
        return basketItemRepository.save(basketItem);
    }
}
