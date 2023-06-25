package com.example.electronicstore.basket;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Basket is not found")
public class BasketNotFoundException extends RuntimeException {

}
