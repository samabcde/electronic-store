package com.example.electronicstore.deal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.io.IOException;

public class DealJsonConverter implements AttributeConverter<Deal, String> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Deal deal) {
        try {
            return objectMapper.writeValueAsString(deal);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert to Json", e);
        }
    }

    @Override
    public Deal convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, Deal.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert from Json", e);
        }
    }
}