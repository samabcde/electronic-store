package com.example.electronicstore.receipt;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.electronicstore.basket.BasketNotFoundException;
import com.example.electronicstore.deal.AppliedDeal;
import com.example.electronicstore.deal.BuyXGetYOffAtNext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

@WebMvcTest(ReceiptController.class)
class ReceiptControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ReceiptService receiptService;
    @Autowired
    ObjectMapper objectMapper;

    @Nested
    public class CalculateReceipt {
        @Test
        void Given_basketIsFound_When_calculateReceipt_Then_returnOk() throws Exception {
            when(receiptService.calculate(new ReceiptRequest(1L))).thenReturn(
                    new Receipt(1L, "customer1",
                            List.of(
                                    new Purchase(1L, "mouse", new BigDecimal("10.5"), 2),
                                    new Purchase(2L, "keyboard", new BigDecimal("15"), 4)
                            ),
                            List.of(
                                    new AppliedDeal(new BuyXGetYOffAtNext(1, 50), 2L, "keyboard", new BigDecimal("15.0"))
                            ),
                            new BigDecimal("66")
                    )
            );
            mockMvc.perform(MockMvcRequestBuilders.get("/receipt?basketId=1", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                {
                                                  "basketId": 1,
                                                  "customerId": "customer1",
                                                  "purchases": [
                                                       {
                                                          "productId": 1,
                                                          "productName": "mouse",
                                                          "productPrice": 10.5,
                                                          "count": 2
                                                       },
                                                       {
                                                          "productId": 2,
                                                          "productName": "keyboard",
                                                          "productPrice": 15,
                                                          "count": 4
                                                       }
                                                  ],
                                                  appliedDeals: [
                                                       {
                                                          "deal": {"dealType": "BUY_X_GET_Y_OFF_AT_NEXT", "buy": 1, "offPercent": 50},
                                                          "productId":2,
                                                          "productName":"keyboard",
                                                          "discount":15.0
                                                       }
                                                  ],
                                                  "totalPrice": 66
                                                }
                            """.stripIndent(), true));
        }

        @Test
        void Given_basketIsNotFound_When_calculateReceipt_Then_returnNotFound() throws Exception {
            when(receiptService.calculate(new ReceiptRequest(1L))).thenThrow(BasketNotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.get("/receipt?basketId=1", 1L))
                    .andExpect(status().isNotFound());
        }
    }
}