package com.company.interview.controller;

import com.company.interview.dto.OrderProductDto;
import com.company.interview.model.OrderProduct;
import com.company.interview.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-product")
public class OrderProductController {
    private final OrderProductService orderProductService;

    @Autowired
    public OrderProductController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    @GetMapping("")
    public ResponseEntity<List<OrderProduct>> getAllOrderProducts() {
        return new ResponseEntity<>(orderProductService.getOrderProducts(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> postOrderProduct(@RequestBody OrderProductDto orderProductDto) {
        if (orderProductService.postOrderProduct(orderProductDto).isPresent()) {
            return new ResponseEntity<>("Product was added", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
