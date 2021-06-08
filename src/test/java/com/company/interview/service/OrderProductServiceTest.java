package com.company.interview.service;

import com.company.interview.dto.OrderProductDto;
import com.company.interview.exception.order.OrderNotFoundException;
import com.company.interview.exception.product.ProductNotFoundException;
import com.company.interview.model.Order;
import com.company.interview.model.OrderProduct;
import com.company.interview.model.Product;
import com.company.interview.repository.OrderProductRepository;
import com.company.interview.repository.OrderRepository;
import com.company.interview.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OrderProductService.class})
@ExtendWith(SpringExtension.class)
public class OrderProductServiceTest {
    @MockBean
    private OrderProductRepository orderProductRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private OrderProductService orderProductService;

    @Test
    public void Should_Get_All_OrderProducts() {
        when(this.orderProductRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(this.orderProductService.getOrderProducts().isEmpty());
        verify(this.orderProductRepository).findAll();
    }

    @Test
    public void Should_Not_Post_Empty_OrderProduct() {
        assertFalse(this.orderProductService.postOrderProduct(new OrderProductDto()).isPresent());
    }

    @Test
    public void Should_Not_Post_OrderProduct_With_Invalid_Attributes() {
        OrderProductDto orderProductDto = mock(OrderProductDto.class);
        when(orderProductDto.hasInvalidAttributes()).thenReturn(true);
        assertFalse(this.orderProductService.postOrderProduct(orderProductDto).isPresent());
        verify(orderProductDto).hasInvalidAttributes();
        assertTrue(this.orderProductService.getOrderProducts().isEmpty());
    }

    @Test
    public void OrderProduct_Test() {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(10L));
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById(any())).thenReturn(ofResult);

        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<OrderProduct>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(100.0);
        order.setOrderId(1L);
        Optional<Order> ofResult1 = Optional.of(order);
        when(this.orderRepository.findById(any())).thenReturn(ofResult1);
        OrderProductDto orderProductDto = mock(OrderProductDto.class);
        when(orderProductDto.getOrderId()).thenReturn(1L);
        when(orderProductDto.getProductId()).thenReturn(1L);
        when(orderProductDto.hasInvalidAttributes()).thenReturn(false);
        assertFalse(this.orderProductService.postOrderProduct(orderProductDto).isPresent());
        verify(this.productRepository).findById(any());
        verify(this.orderRepository).findById(any());
        verify(orderProductDto).getOrderId();
        verify(orderProductDto).getProductId();
        verify(orderProductDto).hasInvalidAttributes();
        assertTrue(this.orderProductService.getOrderProducts().isEmpty());
    }

    @Test
    public void Should_Throw_ProductNotFoundException() {
        when(this.productRepository.findById(any())).thenReturn(Optional.empty());

        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(1L);
        Optional<Order> ofResult = Optional.of(order);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        OrderProductDto orderProductDto = mock(OrderProductDto.class);
        when(orderProductDto.getOrderId()).thenReturn(1L);
        when(orderProductDto.getProductId()).thenReturn(1L);
        when(orderProductDto.hasInvalidAttributes()).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> this.orderProductService.postOrderProduct(orderProductDto));
        verify(this.productRepository).findById(any());
        verify(orderProductDto, times(2)).getProductId();
        verify(orderProductDto).hasInvalidAttributes();
    }

    @Test
    public void Should_Post_OrderProduct() {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Product");
        BigDecimal valueOfResult = BigDecimal.valueOf(10L);
        product.setPrice(valueOfResult);
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById(any())).thenReturn(ofResult);

        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<OrderProduct>());
        order.setIsDone(false);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(100.0);
        order.setOrderId(1L);
        Optional<Order> ofResult1 = Optional.of(order);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(new HashSet<OrderProduct>());
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(100.0);
        order1.setOrderId(1L);
        when(this.orderRepository.save(any())).thenReturn(order1);
        when(this.orderRepository.findById(any())).thenReturn(ofResult1);

        Order order2 = new Order();
        order2.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order2.setOrderProducts(new HashSet<OrderProduct>());
        order2.setIsDone(true);
        order2.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order2.setTotalPrice(100.0);
        order2.setOrderId(1L);

        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Product");
        product1.setPrice(BigDecimal.valueOf(10L));

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderProductId(1L);
        orderProduct.setQuantity(5L);
        orderProduct.setPrice(BigDecimal.valueOf(10L));
        orderProduct.setOrder(order2);
        orderProduct.setProduct(product1);
        when(this.orderProductRepository.save(any())).thenReturn(orderProduct);
        OrderProductDto orderProductDto = mock(OrderProductDto.class);
        when(orderProductDto.getQuantity()).thenReturn(5L);
        when(orderProductDto.getOrderId()).thenReturn(1L);
        when(orderProductDto.getProductId()).thenReturn(1L);
        when(orderProductDto.hasInvalidAttributes()).thenReturn(false);
        Optional<OrderProduct> actualPostOrderProductResult = this.orderProductService.postOrderProduct(orderProductDto);
        assertTrue(actualPostOrderProductResult.isPresent());
        OrderProduct getResult = actualPostOrderProductResult.get();
        Order order3 = getResult.getOrder();
        assertSame(order, order3);
        assertEquals(5L, getResult.getQuantity().longValue());
        assertSame(product, getResult.getProduct());
        BigDecimal price = getResult.getPrice();
        assertSame(valueOfResult, price);
        assertEquals("10", price.toString());
        assertEquals(1, order3.getOrderProducts().size());
        assertEquals(50.0, order3.getTotalPrice().doubleValue());
        verify(this.productRepository).findById(any());
        verify(this.orderRepository).findById(any());
        verify(this.orderRepository).save(any());
        verify(this.orderProductRepository).save(any());
        verify(orderProductDto).getOrderId();
        verify(orderProductDto).getProductId();
        verify(orderProductDto).getQuantity();
        verify(orderProductDto).hasInvalidAttributes();
    }

    @Test
    public void OrderProduct_General_Test() {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Product");
        product.setPrice(null);
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById(any())).thenReturn(ofResult);

        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(1L);
        when(this.orderRepository.save(any())).thenReturn(order);
        when(this.orderRepository.findById(any())).thenReturn(Optional.empty());
        when(this.orderProductRepository.save(any())).thenThrow(new OrderNotFoundException(1L));
        OrderProductDto orderProductDto = mock(OrderProductDto.class);
        when(orderProductDto.getQuantity()).thenReturn(1L);
        when(orderProductDto.getOrderId()).thenReturn(1L);
        when(orderProductDto.getProductId()).thenReturn(1L);
        when(orderProductDto.hasInvalidAttributes()).thenReturn(false);
        assertThrows(OrderNotFoundException.class, () -> this.orderProductService.postOrderProduct(orderProductDto));
        verify(this.productRepository).findById(any());
        verify(this.orderRepository).findById(any());
        verify(orderProductDto, times(2)).getOrderId();
        verify(orderProductDto).getProductId();
        verify(orderProductDto).hasInvalidAttributes();
    }
}

