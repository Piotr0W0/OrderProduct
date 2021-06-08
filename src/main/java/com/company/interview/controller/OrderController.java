package com.company.interview.controller;

import com.company.interview.dto.OrderPeriod;
import com.company.interview.model.Order;
import com.company.interview.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/period")
    public ResponseEntity<List<Order>> getOrdersFromPeriod(OrderPeriod orderPeriod) {
        return new ResponseEntity<>(orderService.getOrdersFromPeriod(orderPeriod), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        Optional<Order> order = orderService.getOrder(orderId);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("")
    public ResponseEntity<String> openOrder() {
        Order order = orderService.openOrder();
        return new ResponseEntity<>("Order " + order.getOrderId() + " was created", HttpStatus.CREATED);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<String> closeOrder(@PathVariable Long orderId) {
        Long closedOrderId = orderService.closeOrder(orderId);
        if (closedOrderId > 0) {
            return new ResponseEntity<>("Order " + closedOrderId + " was closed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
