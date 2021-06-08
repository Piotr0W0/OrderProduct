package com.company.interview.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_products")
@Getter
@Setter
public class OrderProduct {
    @Id
    @Column(name = "order_product_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.ALL) // IMPORTANT
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL) // IMPORTANT
    @JoinColumn(name = "order_id")
    private Order order;
}
