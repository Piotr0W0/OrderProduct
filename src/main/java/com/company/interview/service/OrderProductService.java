package com.company.interview.service;

import com.company.interview.dto.OrderProductDto;
import com.company.interview.model.Order;
import com.company.interview.model.OrderProduct;
import com.company.interview.model.Product;
import com.company.interview.repository.OrderProductRepository;
import com.company.interview.repository.OrderRepository;
import com.company.interview.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public Optional<OrderProduct> postOrderProduct(OrderProductDto orderProductDto) {
        if (!orderProductDto.hasInvalidAttributes()) {
            Optional<Product> optionalProduct = productRepository.findById(orderProductDto.getProductId());
            Optional<Order> order = orderRepository.findById(orderProductDto.getOrderId());
            if (optionalProduct.isPresent() && order.isPresent()) {
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setQuantity(orderProductDto.getQuantity());
                Product product = optionalProduct.get();

                if (order.get().getIsDone()) {
                    orderProduct.setPrice(product.getLastPrice());
                } else {
                    orderProduct.setPrice(product.getPrice());
                }


                orderProduct.setProduct(product);
                orderRepository.save(order.get());
                orderProduct.setOrder(order.get());
                order.get().getOrderProducts().add(orderProduct);
                order.get().setTotalPrice(getTotalPrice(order.get()));
                orderProductRepository.save(orderProduct);
                return Optional.of(orderProduct);
            }
        }
        return Optional.empty();
    }

    private double getTotalPrice(Order order) {
        double bigDecimal = 0.0;
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            bigDecimal += orderProduct.getQuantity() * orderProduct.getPrice().doubleValue();
        }
        return bigDecimal;
    }
}
