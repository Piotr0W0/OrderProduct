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
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OrderProductService.class})
@ExtendWith(SpringExtension.class)
public class OrderProductServiceTest {
    @MockBean
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderProductService orderProductService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void Test_Constructor() {
        assertTrue((new OrderProductService(mock(OrderProductRepository.class), mock(ProductRepository.class),
                mock(OrderRepository.class))).getOrderProducts().isEmpty());
    }

    @Test
    public void Test_GetOrderProducts() {
        when(this.orderProductRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(this.orderProductService.getOrderProducts().isEmpty());
        verify(this.orderProductRepository).findAll();
    }

    @Test
    public void Test_AddOrderProduct_1() {
        assertThrows(BadRequestException.class, () -> this.orderProductService.addOrderProduct(new OrderProductDto()));
    }

    @Test
    public void Test_AddOrderProduct_2() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(42L));
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        Optional<Order> ofResult1 = Optional.of(order);
        when(this.orderRepository.findById(any())).thenReturn(ofResult1);
        OrderProductDto orderProductDto = mock(OrderProductDto.class);
        when(orderProductDto.getQuantity()).thenReturn(1L);
        when(orderProductDto.getOrderId()).thenReturn(1L);
        when(orderProductDto.getProductId()).thenReturn(1L);
        assertThrows(BadRequestException.class, () -> this.orderProductService.addOrderProduct(orderProductDto));
        verify(this.productRepository).findById(any());
        verify(this.orderRepository).findById(any());
        verify(orderProductDto, times(3)).getOrderId();
        verify(orderProductDto, times(2)).getProductId();
        verify(orderProductDto).getQuantity();
    }

    @Test
    public void Test_AddOrderProduct_3() {
        when(this.productRepository.findById(any())).thenReturn(Optional.empty());
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<OrderProduct>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        Optional<Order> ofResult = Optional.of(order);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        OrderProductDto orderProductDto = mock(OrderProductDto.class);
        when(orderProductDto.getQuantity()).thenReturn(1L);
        when(orderProductDto.getOrderId()).thenReturn(1L);
        when(orderProductDto.getProductId()).thenReturn(1L);
        assertThrows(ProductNotFoundException.class, () -> this.orderProductService.addOrderProduct(orderProductDto));
        verify(this.productRepository).findById(any());
        verify(orderProductDto).getOrderId();
        verify(orderProductDto, times(3)).getProductId();
        verify(orderProductDto).getQuantity();
    }

    @Test
    public void Test_AddOrderProduct_4() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        product.setPrice(valueOfResult);
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(false);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        Optional<Order> ofResult1 = Optional.of(order);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(new HashSet<>());
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(10.0);
        order1.setOrderId(123L);
        when(this.orderRepository.save(any())).thenReturn(order1);
        when(this.orderRepository.findById(any())).thenReturn(ofResult1);

        Order order2 = new Order();
        order2.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order2.setOrderProducts(new HashSet<>());
        order2.setIsDone(true);
        order2.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order2.setTotalPrice(10.0);
        order2.setOrderId(123L);

        Product product1 = new Product();
        product1.setProductId(123L);
        product1.setName("Product1");
        product1.setPrice(BigDecimal.valueOf(42L));

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderProductId(123L);
        orderProduct.setQuantity(1L);
        orderProduct.setPrice(BigDecimal.valueOf(42L));
        orderProduct.setOrder(order2);
        orderProduct.setProduct(product1);
        when(this.orderProductRepository.save(any())).thenReturn(orderProduct);
        OrderProductDto orderProductDto = mock(OrderProductDto.class);
        when(orderProductDto.getQuantity()).thenReturn(1L);
        when(orderProductDto.getOrderId()).thenReturn(1L);
        when(orderProductDto.getProductId()).thenReturn(1L);
        Optional<OrderProduct> actualAddOrderProductResult = this.orderProductService.addOrderProduct(orderProductDto);
        assertTrue(actualAddOrderProductResult.isPresent());
        OrderProduct getResult = actualAddOrderProductResult.get();
        Order order3 = getResult.getOrder();
        assertSame(order, order3);
        assertEquals(1L, getResult.getQuantity().longValue());
        assertSame(product, getResult.getProduct());
        BigDecimal price = getResult.getPrice();
        assertSame(valueOfResult, price);
        assertEquals("42", price.toString());
        assertEquals(1, order3.getOrderProducts().size());
        assertEquals(42.0, order3.getTotalPrice().doubleValue());
        verify(this.productRepository).findById(any());
        verify(this.orderRepository).findById(any());
        verify(this.orderRepository).save(any());
        verify(this.orderProductRepository).save(any());
        verify(orderProductDto, times(2)).getOrderId();
        verify(orderProductDto, times(2)).getProductId();
        verify(orderProductDto, times(2)).getQuantity();
    }

    @Test
    public void Test_AddOrderProduct_5() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(null);
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById(any())).thenReturn(ofResult);

        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<OrderProduct>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        when(this.orderRepository.save(any())).thenReturn(order);
        when(this.orderRepository.findById(any())).thenReturn(Optional.empty());
        when(this.orderProductRepository.save(any())).thenThrow(new OrderNotFoundException(1L));
        OrderProductDto orderProductDto = mock(OrderProductDto.class);
        when(orderProductDto.getQuantity()).thenReturn(1L);
        when(orderProductDto.getOrderId()).thenReturn(1L);
        when(orderProductDto.getProductId()).thenReturn(1L);
        assertThrows(OrderNotFoundException.class, () -> this.orderProductService.addOrderProduct(orderProductDto));
        verify(this.productRepository).findById(any());
        verify(this.orderRepository).findById(any());
        verify(orderProductDto, times(3)).getOrderId();
        verify(orderProductDto, times(2)).getProductId();
        verify(orderProductDto).getQuantity();
    }
}

