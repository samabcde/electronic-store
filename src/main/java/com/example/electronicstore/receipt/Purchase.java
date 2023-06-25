package com.example.electronicstore.receipt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer count;
}
