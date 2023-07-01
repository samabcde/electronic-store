package com.example.electronicstore.deal;

import com.example.electronicstore.basket.Basket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public final class BuySomeGetSomeFree extends Deal {
    // e.g. Monitor and a Keyboard
    private Set<Long> broughtProductIds;
    // e.g. Free Headset
    private List<Long> freeProductIds;

    public BuySomeGetSomeFree() {
        super(DealType.BUY_SOME_GET_SOME_FREE);
    }

    public BuySomeGetSomeFree(Set<Long> broughtProductIds, List<Long> freeProductIds) {
        super(DealType.BUY_SOME_GET_SOME_FREE);
        this.broughtProductIds = broughtProductIds;
        this.freeProductIds = freeProductIds;
    }

    @Override
    public FreeDeal calculate(Basket basket) {
        Set<Long> productIds = basket.getBasketItems().stream()
                .filter(basketItem -> basketItem.getCount() > 0)
                .map(basketItem -> basketItem.getProduct().getId()).collect(Collectors.toSet());
        if (productIds.containsAll(broughtProductIds)) {
            return new FreeDeal(freeProductIds);
        }
        return new FreeDeal(Collections.emptyList());
    }
}
