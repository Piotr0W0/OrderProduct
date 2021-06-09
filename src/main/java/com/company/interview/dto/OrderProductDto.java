package com.company.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"orderId", "productId", "quantity"})
public class OrderProductDto {
    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    private Long quantity;

    @Override
    public String toString() {
        return "OrderProductRequest { " +
                "order_id = " + orderId +
                ", product_id = " + productId +
                ", quantity = " + quantity +
                " } ";
    }
}
