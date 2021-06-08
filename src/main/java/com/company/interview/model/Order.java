package com.company.interview.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"orderId", "totalPrice", "orderDate", "modificationDate", "isDone", "orderProducts"})
public class Order {
    @Id
    @Column(name = "order_id", nullable = false, updatable = false)
    @JsonProperty("order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "order_date", nullable = false)
    @JsonProperty("order_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime orderDate;

    @Column(name = "modification_date", nullable = false)
    @JsonProperty("modification_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modificationDate;

    @Column(name = "total_price", nullable = false)
    @JsonProperty("total_price")
    private Double totalPrice;

    @Column(name = "is_done", nullable = false)
    @JsonProperty("is_done")
    private Boolean isDone = false;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty("order_products")
    private Set<OrderProduct> orderProducts;

    public Order(LocalDateTime orderDate, LocalDateTime modificationDate, Double totalPrice, Set<OrderProduct> orderProducts) {
        this.orderDate = orderDate;
        this.modificationDate = modificationDate;
        this.totalPrice = totalPrice;
        this.orderProducts = orderProducts;
    }
}