package com.example.electronicstore.product;

import com.example.electronicstore.deal.Deal;
import com.example.electronicstore.deal.DealJsonConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    public Product(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @NotNull
    private String name;

    @NotNull
    @Max(100_000_000L)
    @Min(0)
    private BigDecimal price;

    @Convert(converter = DealJsonConverter.class)
    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    private Deal deal;
}
