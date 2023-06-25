package com.example.electronicstore.basket;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface BasketRepository extends CrudRepository<Basket, Long> {

}
