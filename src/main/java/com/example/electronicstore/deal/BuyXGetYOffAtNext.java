package com.example.electronicstore.deal;

import com.example.electronicstore.basket.Basket;
import com.example.electronicstore.basket.BasketItem;
import com.example.electronicstore.product.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.MathContext;

@Data
@EqualsAndHashCode(callSuper = false)
public final class BuyXGetYOffAtNext extends Deal {
    public BuyXGetYOffAtNext() {
        super(DealType.BUY_X_GET_Y_OFF_AT_NEXT);
    }

    public BuyXGetYOffAtNext(Integer buy, Integer offPercent) {
        super(DealType.BUY_X_GET_Y_OFF_AT_NEXT);
        this.buy = buy;
        this.offPercent = offPercent;
    }

    @NotNull
    @Min(0)
    private Integer buy;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer offPercent;

    @Override
    public AppliedDeal calculate(BasketItem basketItem) {
        Product product = basketItem.getProduct();
        BigDecimal originalPrice = product.getPrice();
        Integer count = basketItem.getCount();
        int noOfItemWithDiscount = count / (buy + 1);
        BigDecimal discount = originalPrice
                .multiply(BigDecimal.valueOf(noOfItemWithDiscount), MathContext.DECIMAL64)
                .multiply(BigDecimal.valueOf(offPercent), MathContext.DECIMAL64)
                .divide(BigDecimal.valueOf(100L), MathContext.DECIMAL64);
        return new AppliedDeal(this, product.getId(), product.getName(), discount);
    }

    @Override
    public FreeDeal calculate(Basket basket) {
        return null;
    }
}
