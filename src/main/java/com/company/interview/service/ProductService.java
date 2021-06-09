package com.company.interview.service;

import com.company.interview.dto.ProductDto;
import com.company.interview.exception.badrequest.BadRequestException;
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
import java.util.ConcurrentModificationException;
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

    public Optional<Product> addProduct(ProductDto productDto) {
        if (Validator.checkAttributes(productDto.getName(), productDto.getPrice())) {
            Product product = new Product();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            productRepository.save(product);
            return Optional.of(product);
        }
        throw new BadRequestException("Bad request data: " + productDto);
    }

    public Product updateProduct(Long productId, ProductDto productDto) {
        if (Validator.checkId(productId)) {
            Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
            if (Validator.checkAttributes(productDto.getName(), productDto.getPrice())) {
                product.setName(productDto.getName());
                product.setPrice(productDto.getPrice());
                try {
                    for (Order order : orderRepository.findAll()) {
                        if (!order.getIsDone()) {
                            for (OrderProduct orderProduct : order.getOrderProducts()) {
                                if (orderProduct.getProduct() == product) {
                                    orderProduct.setPrice(productDto.getPrice());
                                    orderProductRepository.save(orderProduct);
                                    order.setTotalPrice(order.getOrderProducts()
                                            .stream()
                                            .mapToDouble(d -> d.getQuantity() * d.getPrice().doubleValue())
                                            .sum());
                                    order.setModificationDate(LocalDateTime.now());
                                    orderRepository.save(order);
                                }
                            }
                        }
                    }
                } catch (ConcurrentModificationException e) {
                    productRepository.save(product);
                    return product;
                }
                productRepository.save(product);
                return product;
            }
            throw new BadRequestException("Bad request data: " + productDto);
        }
        throw new BadRequestException("Bad request data - order_id: " + productId);
    }
}
