package com.example.electronicstore.basket;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Basket Item is not found")
public class BasketItemNotFoundException extends RuntimeException {

}
