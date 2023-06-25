package com.example.electronicstore.basket;

import static io.restassured.RestAssured.DEFAULT_PORT;
import static io.restassured.RestAssured.given;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import com.example.electronicstore.deal.BuyXGetYOffAtNext;
import com.example.electronicstore.deal.None;
import com.example.electronicstore.product.Product;
import com.example.electronicstore.product.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@DirtiesContext
public class CustomerAcceptanceTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductRepository productRepository;

    Product mouse;
    Product keyboard;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        RestAssured.port = DEFAULT_PORT;
        mouse = productRepository.save(new Product(null, "mouse", new BigDecimal("5.5"), new None()));
        keyboard = productRepository.save(new Product(null, "keyboard", new BigDecimal("8.0"), new BuyXGetYOffAtNext(1, 25)));
    }

    @Test
    void customerFlow() throws JsonProcessingException {
        Long basketId = createBasketIsOkAndReturnId(new Basket(null, "customer1", emptyList()));
        addItemIsOkay(new BasketItemRequest(mouse.getId(), basketId), 1);
        addItemIsOkay(new BasketItemRequest(mouse.getId(), basketId), 2);
        addItemIsOkay(new BasketItemRequest(keyboard.getId(), basketId), 1);
        addItemIsOkay(new BasketItemRequest(keyboard.getId(), basketId), 2);
        removeItemIsOkay(new BasketItemRequest(mouse.getId(), basketId), 1);
        calculateReceiptIsOk(basketId, new BigDecimal("19.5"));
    }

    private Long createBasketIsOkAndReturnId(Basket basket) throws JsonProcessingException {
        return Long.valueOf((Integer) given().contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(basket))
                .post("/basket")
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().get("id"));
    }

    private void addItemIsOkay(BasketItemRequest basketItemRequest, Integer expectedCount) throws JsonProcessingException {
        given().contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(basketItemRequest))
                .post("/basket/basket-item")
                .then()
                .statusCode(200)
                .body("count", equalTo(expectedCount));
    }

    private void removeItemIsOkay(BasketItemRequest basketItemRequest, Integer expectedCount) throws JsonProcessingException {
        given().contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(basketItemRequest))
                .delete("/basket/basket-item")
                .then()
                .statusCode(200)
                .body("count", equalTo(expectedCount));

    }

    private void calculateReceiptIsOk(Long basketId, BigDecimal expectedPrice) {
        given().contentType(ContentType.JSON)
                .get("/receipt?basketId=" + basketId)
                .then()
                .statusCode(200)
                .body("totalPrice", equalTo(expectedPrice.floatValue()));
    }
}
