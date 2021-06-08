package com.company.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String name;
    private BigDecimal price;

    public boolean hasInvalidAttributes() {
        return name == null || price == null || price.doubleValue() < 0.0;
    }
}
