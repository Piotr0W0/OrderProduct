package com.company.interview.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    @JsonAlias("order_id")
    private Long orderId;

    public boolean hasInvalidAttributes() {
        return orderId == null || orderId < 0;
    }
}
