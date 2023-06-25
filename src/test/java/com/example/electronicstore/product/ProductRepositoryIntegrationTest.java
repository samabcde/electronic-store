package com.example.electronicstore.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import com.example.electronicstore.deal.BuyXGetYOffAtNext;
import com.example.electronicstore.deal.Deal;
import com.example.electronicstore.deal.DealType;
import com.example.electronicstore.deal.None;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootTest
class ProductRepositoryIntegrationTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    void save_Then_retrieve() {
        Product product = new Product(null, "mouse", new BigDecimal("50.5"), new None());
        Product saved = productRepository.save(product);
        assertEquals("mouse", saved.getName());
        assertEquals(new BigDecimal("50.5"), saved.getPrice());
        assertEquals(new None(), saved.getDeal());
    }

    @ParameterizedTest
    @MethodSource("deals")
    void savePolymorphicDealAsJson_Then_retrieve(Deal deal) {
        Product product = new Product(null, "mouse", new BigDecimal("50.5"), deal);
        Product saved = productRepository.save(product);
        assertEquals(deal, productRepository.findById(saved.getId()).get().getDeal());
    }

    public static Stream<Arguments> deals() {
        return Arrays.stream(DealType.values()).map(dealType ->
                arguments(switch (dealType) {
                    case NONE -> new None();
                    case BUY_X_GET_Y_OFF_AT_NEXT -> new BuyXGetYOffAtNext(1, 2);
                }));
    }
}