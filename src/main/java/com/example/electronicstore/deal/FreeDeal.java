package com.example.electronicstore.deal;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FreeDeal {
    private List<Long> productIds;
}
