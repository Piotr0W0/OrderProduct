package com.company.interview.service;

import com.company.interview.dto.OrderPeriod;
import com.company.interview.model.Order;
import com.company.interview.model.OrderProduct;
import com.company.interview.repository.OrderProductRepository;
import com.company.interview.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orderRepository.findAll());
    }

    public List<Order> getOrdersFromPeriod(OrderPeriod orderPeriod) {
        if (orderPeriod.hasInvalidAttributes()) {
            orderPeriod.setStartDate(LocalDateTime.now().minus(Duration.ofHours(10L)));
            orderPeriod.setEndDate(LocalDateTime.now());
        }
        List<Order> orders = new ArrayList<>();
        for (Order order : orderRepository.findAll()) {
            //if (orderPeriod.getStartDate().isBefore(LocalDate.from(order.getOrderDate())) && orderPeriod.getEndDate().isAfter(LocalDate.from(order.getOrderDate()))) {
            if (orderPeriod.getStartDate().isBefore(order.getOrderDate()) && orderPeriod.getEndDate().isAfter(order.getOrderDate())) {
                orders.add(order);
            }
        }
        return orders;
    }

    public Optional<Order> getOrder(Long orderId) {
        if (orderId != null && orderId >= 0) {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                order.setOrderDate(LocalDateTime.now());
                order.setModificationDate(LocalDateTime.now());
                order.setTotalPrice(getTotalPrice(order));
                orderRepository.save(order);
                return Optional.of(order);
            }
        }
        return Optional.empty();
    }

    public Order openOrder() {
        Order order = new Order(LocalDateTime.now(), LocalDateTime.now(), 0.0);
        orderRepository.save(order);
        return order;
    }

    public Long closeOrder(Long orderId) {
        if (orderId != null && orderId >= 0) {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isPresent()) {

                optionalOrder.get().setIsDone(true);

                orderRepository.save(optionalOrder.get());

                return optionalOrder.get().getOrderId();
            }
        }
        return -1L;
    }

    private double getTotalPrice(Order order) {
        double bigDecimal = 0.0;
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            bigDecimal += orderProduct.getQuantity() * orderProduct.getPrice().doubleValue();
        }
        return bigDecimal;
    }
}
