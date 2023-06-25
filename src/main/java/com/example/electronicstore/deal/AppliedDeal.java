package com.example.electronicstore.deal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppliedDeal {
    private Deal deal;
    private Long productId;
    private String productName;
    private BigDecimal discount;

    @JsonIgnore
    public boolean isNotNoneDeal() {
        return getDeal().getDealType() != DealType.NONE;
    }
}
