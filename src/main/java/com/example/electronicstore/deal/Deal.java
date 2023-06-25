package com.example.electronicstore.deal;

import com.example.electronicstore.basket.BasketItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "dealType",
        defaultImpl = None.class,
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = None.class, name = "NONE"),
        @JsonSubTypes.Type(value = BuyXGetYOffAtNext.class, name = "BUY_X_GET_Y_OFF_AT_NEXT"),
})
@Data
public abstract sealed class Deal permits None, BuyXGetYOffAtNext {
    private DealType dealType;

    public abstract AppliedDeal calculate(BasketItem basketItem);

}