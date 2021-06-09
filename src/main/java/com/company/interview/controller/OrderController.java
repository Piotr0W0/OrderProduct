package com.company.interview.controller;

import com.company.interview.model.Order;
import com.company.interview.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/period")
    public ResponseEntity<?> getOrdersFromPeriod(@RequestParam(value = "from", defaultValue = "2021-06-10 08:00") String from,
                                                 @RequestParam(value = "to", defaultValue = "2022-06-10 08:00") String to) {
        List<Order> ordersFromPeriod = orderService.getOrdersFromPeriod(from, to);
        return new ResponseEntity<>(ordersFromPeriod, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> openOrder() {
        return new ResponseEntity<>("Order " + orderService.openOrder().getOrderId() + " was created", HttpStatus.CREATED);
    }

    @PatchMapping("/calculate/{orderId}")
    public ResponseEntity<?> calculateOrder(@PathVariable Long orderId) {
        Order order = orderService.calculateOrder(orderId);
        return new ResponseEntity<>("Values for order " + order.getOrderId() + " was updated", HttpStatus.OK);
    }

    @PatchMapping("/close/{orderId}")
    public ResponseEntity<?> closeOrder(@PathVariable Long orderId) {
        Order order = orderService.closeOrder(orderId);
        return new ResponseEntity<>("Order " + order.getOrderId() + " was closed", HttpStatus.OK);
    }
}
