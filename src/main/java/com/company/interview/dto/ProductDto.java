package com.company.interview.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"name", "price"})
public class ProductDto {
    private String name;
    private BigDecimal price;

    @Override
    public String toString() {
        return "ProductRequest { " +
                "name = " + name +
                ", price = " + price +
                " } ";
    }
}
