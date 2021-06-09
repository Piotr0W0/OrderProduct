package com.company.interview.controller;

import com.company.interview.dto.OrderProductDto;
import com.company.interview.model.OrderProduct;
import com.company.interview.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order-product")
public class OrderProductController {
    private final OrderProductService orderProductService;

    @Autowired
    public OrderProductController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrderProducts() {
        List<OrderProduct> orderProducts = orderProductService.getOrderProducts();
        return new ResponseEntity<>(orderProducts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addOrderProduct(@RequestBody OrderProductDto orderProductDto) {
        Optional<OrderProduct> optionalOrderProduct = orderProductService.addOrderProduct(orderProductDto);
        if (optionalOrderProduct.isPresent()) {
            return new ResponseEntity<>("Product was added", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Bad format date", HttpStatus.BAD_REQUEST);
        }
    }
}
