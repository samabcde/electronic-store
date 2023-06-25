package com.example.electronicstore.product;

import static io.restassured.RestAssured.DEFAULT_PORT;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import com.example.electronicstore.deal.BuyXGetYOffAtNext;
import com.example.electronicstore.deal.None;
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
public class AdminAcceptanceTest {
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        RestAssured.port = DEFAULT_PORT;
    }

    @Test
    void adminFlow() throws JsonProcessingException {
        Long id = createProductIsOkAndRetrieveId(new Product(null, "mouse", new BigDecimal("30.9"), new None()));
        retrieveProductIsOk(id, "mouse");
        updateProductIsOk(new Product(Long.valueOf(id), "mouse2", new BigDecimal("32"), new BuyXGetYOffAtNext(1, 10)), id, "mouse2");
        deleteProductIsOk(id);
    }

    private Long createProductIsOkAndRetrieveId(Product product) throws JsonProcessingException {
        return Long.valueOf((Integer) given().contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(product))
                .post("/admin/product")
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().get("id"));
    }

    private void retrieveProductIsOk(Long id, String expectedName) {
        given().contentType(ContentType.JSON)
                .get("/admin/product/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo(expectedName));
    }

    private void updateProductIsOk(Product product, Long id, String expectedName) throws JsonProcessingException {
        given().contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(product))
                .put("/admin/product/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo(expectedName));
    }

    private void deleteProductIsOk(Long id) {
        given().contentType(ContentType.JSON)
                .delete("/admin/product/" + id)
                .then()
                .statusCode(200);
    }

}
