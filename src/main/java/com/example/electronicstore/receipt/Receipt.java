package com.example.electronicstore.receipt;

import com.example.electronicstore.deal.AppliedDeal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    private Long basketId;
    private String customerId;
    private List<Purchase> purchases;
    private List<AppliedDeal> appliedDeals;
    private BigDecimal totalPrice;
}
