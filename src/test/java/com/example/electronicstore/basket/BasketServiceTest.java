package com.example.electronicstore.basket;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import com.example.electronicstore.product.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {
    @Mock
    BasketItemRepository basketItemRepository;
    @Mock
    BasketRepository basketRepository;

    @InjectMocks
    BasketService basketService;

    @Nested
    public class CreateBasket {
        @Test
        void When_createBasket_Then_saveInRepository() {
            Basket input = new Basket(null, "customer1", emptyList());
            Basket created = new Basket(1L, "customer1", emptyList());
            when(basketRepository.save(input)).thenReturn(created);

            Basket actual = basketService.createBasket(input);
            verify(basketRepository).save(input);
            assertEquals(created, actual);
        }
    }

    @Nested
    public class GetBasket {

        @Test
        void Given_basketExist_When_getBasket_Then_returnFromRepository() {
            Basket existing = new Basket(1L, "customer1", emptyList());
            when(basketRepository.findById(1L)).thenReturn(Optional.of(existing));

            Basket actual = basketService.getBasket(1L);

            verify(basketRepository).findById(1L);
            assertEquals(existing, actual);
        }

        @Test
        void Given_basketNotExist_When_getBasket_Then_throwBasketNotFoundException() {
            when(basketRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(BasketNotFoundException.class, () -> basketService.getBasket(1L));

            verify(basketRepository).findById(1L);
        }
    }

    @Nested
    public class AddItem {
        @Test
        void Given_itemNotExist_When_addItem_Then_createItemInRepository() {
            Basket basket = new Basket(1L);
            Product product = new Product(1L);
            when(basketItemRepository.findByBasketAndProduct(basket, product)).thenReturn(Optional.empty());
            BasketItem created = new BasketItem(1L, basket, product, 1, 1L);
            when(basketItemRepository.save(new BasketItem(null, basket, product, 1, null)))
                    .thenReturn(created);

            BasketItem actual = basketService.addItem(1L, 1L);

            assertEquals(created, actual);
        }

        @Test
        void Given_itemExist_When_addItem_Then_incrementCountAndUpdateItemInRepository() {
            Basket basket = new Basket(1L);
            Product product = new Product(1L);
            BasketItem created = new BasketItem(1L, basket, product, 1, 1L);
            when(basketItemRepository.findByBasketAndProduct(basket, product)).thenReturn(Optional.of(created));
            BasketItem updated = new BasketItem(1L, basket, product, 2, 2L);
            when(basketItemRepository.save(new BasketItem(1L, basket, product, 2, 1L)))
                    .thenReturn(updated);

            BasketItem actual = basketService.addItem(1L, 1L);

            assertEquals(updated, actual);
        }
    }

    @Nested
    public class RemoveItem {

        @Test
        void Given_itemNotExist_When_removeItem_Then_throwBaskItemNotFoundException() {
            Basket basket = new Basket(1L);
            Product product = new Product(1L);
            when(basketItemRepository.findByBasketAndProduct(basket, product)).thenReturn(Optional.empty());

            assertThrows(BasketItemNotFoundException.class, () -> basketService.removeItem(1L, 1L));
        }

        @Test
        void Given_itemExist_When_removeItem_Then_decrementCountAndUpdateItemInRepository() {
            Basket basket = new Basket(1L);
            Product product = new Product(1L);
            BasketItem created = new BasketItem(1L, basket, product, 2, 1L);
            when(basketItemRepository.findByBasketAndProduct(basket, product)).thenReturn(Optional.of(created));
            BasketItem updated = new BasketItem(1L, basket, product, 1, 2L);
            when(basketItemRepository.save(new BasketItem(1L, basket, product, 1, 1L)))
                    .thenReturn(updated);

            BasketItem actual = basketService.removeItem(1L, 1L);

            assertEquals(updated, actual);
        }

        @Test
        void Given_itemExistWith0Count_When_removeItem_Then_returnInRepository() {
            Basket basket = new Basket(1L);
            Product product = new Product(1L);
            BasketItem created = new BasketItem(1L, basket, product, 0, 1L);
            when(basketItemRepository.findByBasketAndProduct(basket, product)).thenReturn(Optional.of(created));

            BasketItem actual = basketService.removeItem(1L, 1L);

            verify(basketItemRepository, times(0)).save(any());
            assertEquals(created, actual);
        }
    }

}