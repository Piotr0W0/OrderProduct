package com.company.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderProductDto {
    private Long quantity;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("order_id")
    private Long orderId;

    public boolean hasInvalidAttributes() {
        return quantity == null || quantity <= 0 || productId == null || productId < 0 || orderId == null || orderId < 0;
    }
}
