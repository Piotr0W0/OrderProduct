package com.company.interview.service;

import com.company.interview.dto.ProductDto;
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
public class ProductService {
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public ProductService(OrderProductRepository orderProductRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productRepository.findAll());
    }

    public Optional<Product> postProduct(ProductDto productDto) {
        if (!productDto.hasInvalidAttributes()) {
            Product product = new Product();
            product.setName(productDto.getName());

            product.setPrice(productDto.getPrice());
            product.setLastPrice(productDto.getPrice());

            productRepository.save(product);
            return Optional.of(product);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Product> putProduct(Long productId, ProductDto productDto) {
        if (productId != null && productId >= 0) {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                optionalProduct.get().setName(productDto.getName());

                for (Order order : orderRepository.findAll()) {
                    if (!order.getIsDone()) {
                        for (OrderProduct orderProduct : order.getOrderProducts()) {
                            if (orderProduct.getProduct() == optionalProduct.get()) {
                                orderProduct.setPrice(productDto.getPrice());
                                orderProductRepository.save(orderProduct);
                                order.setTotalPrice(getTotalPrice(order));
                                orderRepository.save(order);
                            }
                        }
                    }
                }
                optionalProduct.get().setPrice(productDto.getPrice()); // CHANGE

                productRepository.save(optionalProduct.get());

                return optionalProduct;
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
