package com.example.electronicstore.basket;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.electronicstore.product.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BasketController.class)
class BasketControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    BasketService basketService;
    @Autowired
    ObjectMapper objectMapper;

    @Nested
    public class GetBasket {
        @Test
        void Given_basketIsFound_When_getBasket_Then_returnOk() throws Exception {
            when(basketService.getBasket(1L)).thenReturn(new Basket(1L, "customer1", emptyList()));
            mockMvc.perform(MockMvcRequestBuilders.get("/basket/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                {
                                                  "id": 1,
                                                  "customerId": "customer1",
                                                  "basketItems": []
                                                }
                            """.stripIndent(), true));
        }

        @Test
        void Given_basketNotFound_When_getBasket_Then_returnNotFound() throws Exception {
            when(basketService.getBasket(1L)).thenThrow(BasketNotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.get("/basket/{id}", 1L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class CreateBasket {
        @Test
        void Given_validBasket_When_createBasket_Then_returnOk() throws Exception {
            when(basketService.createBasket(new Basket(null, "customer1", emptyList())))
                    .thenReturn(new Basket(1L, "customer1", emptyList()));
            mockMvc.perform(MockMvcRequestBuilders.post("/basket")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "id": null,
                                                  "customerId": "customer1",
                                                  "basketItems": []
                                                }
                                    """.stripIndent()))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                {
                                                  "id": 1,
                                                  "customerId": "customer1",
                                                  "basketItems": []
                                                }
                            """, true));
        }

        @ParameterizedTest
        @CsvSource(useHeadersInDisplayName = true, textBlock = """
                customerId, basketItems, field, validationMessage
                customerIdLongLongLongLongLongLongLongLongLongLong1, [], customerId, size must be between 0 and 50
                customerId1, null, basketItems, must not be null
                """)
        void Given_invalidProduct_When_createProduct_Then_returnBadRequest(String customerId, String basketItems, String field, String validationMessage) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/basket")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "id": null,
                                                  "customerId": "%s",
                                                  "basketItems": %s
                                                }
                                    """.stripIndent().formatted(customerId, basketItems)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                                                {
                                                  "%s": "%s"
                                                }
                            """.stripIndent().formatted(field, validationMessage), true));
        }
    }

    @Nested
    public class AddBasketItem {

        @Test
        void Given_validBasket_When_addBasketItem_Then_returnOk() throws Exception {
            when(basketService.addItem(2L, 3L))
                    .thenReturn(new BasketItem(1L, new Basket(2L), new Product(3L), 1, 1L));
            mockMvc.perform(MockMvcRequestBuilders.post("/basket/basket-item")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "basketId": 2,
                                                  "productId": 3
                                                }
                                    """.stripIndent()))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                {
                                                  "basketId": 2,
                                                  "productId": 3,
                                                  "count": 1
                                                }
                            """, true));
        }

        @ParameterizedTest
        @CsvSource(useHeadersInDisplayName = true, textBlock = """
                basketId, productId, field, validationMessage
                null, 1, basketId, must not be null
                1, null, productId, must not be null
                """)
        void Given_invalidBasketItem_When_addBasketItem_Then_returnBadRequest(String basketId, String productId, String field, String validationMessage) throws Exception {
            when(basketService.addItem(2L, 3L))
                    .thenReturn(new BasketItem(1L, new Basket(2L), new Product(3L), 1, 1L));
            mockMvc.perform(MockMvcRequestBuilders.post("/basket/basket-item")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "basketId": %s,
                                                  "productId": %s
                                                }
                                    """.stripIndent().formatted(basketId, productId)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                                                {
                                                  "%s": "%s"
                                                }
                            """.stripIndent().formatted(field, validationMessage), true));
        }
    }

    @Nested
    public class RemoveBasketItem {

        @Test
        void Given_validBasket_When_removeBasketItem_Then_returnOk() throws Exception {
            when(basketService.removeItem(2L, 3L))
                    .thenReturn(new BasketItem(1L, new Basket(2L), new Product(3L), 1, 1L));
            mockMvc.perform(MockMvcRequestBuilders.delete("/basket/basket-item")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "basketId": 2,
                                                  "productId": 3
                                                }
                                    """.stripIndent()))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                {
                                                  "basketId": 2,
                                                  "productId": 3,
                                                  "count": 1
                                                }
                            """, true));
        }

        @ParameterizedTest
        @CsvSource(useHeadersInDisplayName = true, textBlock = """
                basketId, productId, field, validationMessage
                null, 1, basketId, must not be null
                1, null, productId, must not be null
                """)
        void Given_invalidBasketItem_When_removeBasketItem_Then_returnBadRequest(String basketId, String productId, String field, String validationMessage) throws Exception {
            when(basketService.removeItem(2L, 3L))
                    .thenReturn(new BasketItem(1L, new Basket(2L), new Product(3L), 1, 1L));
            mockMvc.perform(MockMvcRequestBuilders.delete("/basket/basket-item")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "basketId": %s,
                                                  "productId": %s
                                                }
                                    """.stripIndent().formatted(basketId, productId)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                                                {
                                                  "%s": "%s"
                                                }
                            """.stripIndent().formatted(field, validationMessage), true));
        }


        @Test
        void Given_notExistBasketItem_When_removeBasketItem_Then_returnNotFound() throws Exception {
            when(basketService.removeItem(2L, 3L))
                    .thenThrow(BasketItemNotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.delete("/basket/basket-item")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "basketId": 2,
                                                  "productId": 3
                                                }
                                    """.stripIndent()))
                    .andExpect(status().isNotFound());
        }
    }
}