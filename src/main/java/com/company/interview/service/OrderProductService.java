package com.company.interview.service;

import com.company.interview.dto.OrderProductDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderProductService {
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderProductService(OrderProductRepository orderProductRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderProduct> getOrderProducts() {
        return new ArrayList<>(orderProductRepository.findAll());
    }

    public Optional<OrderProduct> addOrderProduct(OrderProductDto orderProductDto) {
        if (Validator.checkAttributes(orderProductDto.getProductId(), orderProductDto.getOrderId(), orderProductDto.getQuantity())) {
            Product product = productRepository.findById(orderProductDto.getProductId()).orElseThrow(() ->
                    new ProductNotFoundException(orderProductDto.getProductId()));
            Order order = orderRepository.findById(orderProductDto.getOrderId()).orElseThrow(() ->
                    new OrderNotFoundException(orderProductDto.getOrderId()));
            if (!order.getIsDone()) {
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setQuantity(orderProductDto.getQuantity());
                orderProduct.setPrice(product.getPrice());
                orderProduct.setProduct(product);
                orderRepository.save(order);
                orderProduct.setOrder(order);
                order.getOrderProducts().add(orderProduct);
                order.setTotalPrice(order.getOrderProducts()
                        .stream()
                        .mapToDouble(d -> d.getQuantity() * d.getPrice()
                                .doubleValue())
                        .sum());
                order.setModificationDate(LocalDateTime.now());
                orderProductRepository.save(orderProduct);
                return Optional.of(orderProduct);
            }
            throw new BadRequestException("Order " + orderProductDto.getOrderId() + " already closed");
        }
        throw new BadRequestException("Bad request data: " + orderProductDto);
    }
}
