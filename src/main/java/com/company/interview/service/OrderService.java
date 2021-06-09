package com.company.interview.service;

import com.company.interview.exception.badrequest.BadRequestException;
import com.company.interview.exception.order.OrderNotFoundException;
import com.company.interview.exception.product.ProductNotFoundException;
import com.company.interview.model.Order;
import com.company.interview.model.OrderProduct;
import com.company.interview.model.Product;
import com.company.interview.repository.OrderProductRepository;
import com.company.interview.repository.OrderRepository;
import com.company.interview.repository.ProductRepository;
import com.company.interview.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orderRepository.findAll());
    }

    public List<Order> getOrdersFromPeriod(String from, String to) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate;
        LocalDateTime endDate;
        try {
            startDate = LocalDateTime.parse(from, formatter);
            endDate = LocalDateTime.parse(to, formatter);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Bad format date from: " + from + " to: " + to);
        }
        if (startDate.isBefore(endDate)) {
            return orderRepository.findAll().stream()
                    .filter(o -> startDate.isBefore(o.getOrderDate()) && endDate.isAfter(o.getOrderDate()))
                    .collect(Collectors.toList());
        }
        throw new BadRequestException("End date " + to + " is before start date " + from);
    }

    public Order openOrder() {
        Order order = new Order(LocalDateTime.now(), LocalDateTime.now(), 0.0, new HashSet<>());
        orderRepository.save(order);
        return order;
    }

    public Order calculateOrder(Long orderId) {
        if (Validator.checkId(orderId)) {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            if (order.getIsDone()) {
                for (OrderProduct orderProduct : order.getOrderProducts()) {
                    Product product = productRepository.findById(orderProduct.getProduct().getProductId()).orElseThrow(() ->
                            new ProductNotFoundException(orderProduct.getProduct().getProductId()));
                    orderProduct.setPrice(product.getPrice());
                    orderProductRepository.save(orderProduct);
                    order.setTotalPrice(order.getOrderProducts()
                            .stream()
                            .mapToDouble(d -> d.getQuantity() * d.getPrice().doubleValue())
                            .sum());
                    order.setModificationDate(LocalDateTime.now());
                    orderRepository.save(order);
                }
                orderRepository.save(order);
                return order;
            }
            throw new BadRequestException("Order " + orderId + " already up to date");
        }
        throw new BadRequestException("Bad request data - order_id: " + orderId);
    }

    public Order closeOrder(Long orderId) {
        if (Validator.checkId(orderId)) {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            if (!order.getIsDone()) {
                order.setIsDone(true);
                order.setModificationDate(LocalDateTime.now());
                orderRepository.save(order);
                return order;
            }
            throw new BadRequestException("Order " + orderId + " already closed");
        }
        throw new BadRequestException("Bad request data - order_id: " + orderId);
    }
}
