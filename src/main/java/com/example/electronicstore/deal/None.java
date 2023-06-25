package com.example.electronicstore.deal;

import com.example.electronicstore.basket.BasketItem;
import com.example.electronicstore.product.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public final class None extends Deal {

    public None() {
        super(DealType.NONE);
    }

    @Override
    public AppliedDeal calculate(BasketItem basketItem) {
        Product product = basketItem.getProduct();
        return new AppliedDeal(this, product.getId(), product.getName(), BigDecimal.ZERO);
    }
}
