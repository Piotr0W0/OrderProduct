package com.company.interview.service;

import com.company.interview.exception.badrequest.BadRequestException;
import com.company.interview.exception.order.OrderNotFoundException;
import com.company.interview.model.Order;
import com.company.interview.model.OrderProduct;
import com.company.interview.repository.OrderProductRepository;
import com.company.interview.repository.OrderRepository;
import com.company.interview.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(SpringExtension.class)
public class OrderServiceTest {
    @MockBean
    private OrderProductRepository orderProductRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void Test_Constructor() {
        assertTrue((new OrderService(mock(OrderRepository.class), mock(OrderProductRepository.class),
                mock(ProductRepository.class))).getAllOrders().isEmpty());
    }

    @Test
    public void Test_GetAllOrders() {
        when(this.orderRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(this.orderService.getAllOrders().isEmpty());
        verify(this.orderRepository).findAll();
    }

    @Test
    public void Test_GetOrdersFromPeriod() {
        assertThrows(BadRequestException.class,
                () -> this.orderService.getOrdersFromPeriod("someFromPeriod", "someToPeriod"));
    }

    @Test
    public void Test_OpenOrder() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        when(this.orderRepository.save(any())).thenReturn(order);
        Order actualOpenOrderResult = this.orderService.openOrder();
        assertFalse(actualOpenOrderResult.getIsDone());
        assertEquals(0.0, actualOpenOrderResult.getTotalPrice().doubleValue());
        assertTrue(actualOpenOrderResult.getOrderProducts().isEmpty());
        verify(this.orderRepository).save(any());
    }

    @Test
    public void Test_CalculateOrder_1() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        Optional<Order> ofResult = Optional.of(order);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(new HashSet<>());
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(10.0);
        order1.setOrderId(123L);
        when(this.orderRepository.save(any())).thenReturn(order1);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        assertSame(order, this.orderService.calculateOrder(123L));
        verify(this.orderRepository).findById(any());
        verify(this.orderRepository).save(any());
    }

    @Test
    public void Test_CalculateOrder_2() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(false);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        Optional<Order> ofResult = Optional.of(order);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(new HashSet<>());
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(10.0);
        order1.setOrderId(123L);
        when(this.orderRepository.save(any())).thenReturn(order1);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        assertThrows(BadRequestException.class, () -> this.orderService.calculateOrder(123L));
        verify(this.orderRepository).findById(any());
    }

    @Test
    public void Test_CalculateOrder_3() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        when(this.orderRepository.save(any())).thenReturn(order);
        when(this.orderRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> this.orderService.calculateOrder(123L));
        verify(this.orderRepository).findById(any());
    }

    @Test
    public void Test_CalculateOrder_4() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        Optional<Order> ofResult = Optional.of(order);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(new HashSet<>());
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(10.0);
        order1.setOrderId(123L);
        when(this.orderRepository.save(any())).thenReturn(order1);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        assertThrows(BadRequestException.class, () -> this.orderService.calculateOrder(0L));
    }

    @Test
    public void Test_CloseOrder_1() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<OrderProduct>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        Optional<Order> ofResult = Optional.of(order);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        assertThrows(BadRequestException.class, () -> this.orderService.closeOrder(123L));
        verify(this.orderRepository).findById(any());
    }

    @Test
    public void Test_CloseOrder_2() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(false);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        Optional<Order> ofResult = Optional.of(order);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(new HashSet<>());
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(10.0);
        order1.setOrderId(123L);
        when(this.orderRepository.save(any())).thenReturn(order1);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        Order actualCloseOrderResult = this.orderService.closeOrder(123L);
        assertSame(order, actualCloseOrderResult);
        assertTrue(actualCloseOrderResult.getIsDone());
        verify(this.orderRepository).findById(any());
        verify(this.orderRepository).save(any());
    }

    @Test
    public void Test_CloseOrder_3() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        when(this.orderRepository.save(any())).thenReturn(order);
        when(this.orderRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> this.orderService.closeOrder(123L));
        verify(this.orderRepository).findById(any());
    }

    @Test
    public void Test_CloseOrder_4() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(false);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(123L);
        Optional<Order> ofResult = Optional.of(order);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(new HashSet<>());
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(10.0);
        order1.setOrderId(123L);
        when(this.orderRepository.save(any())).thenReturn(order1);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        assertThrows(BadRequestException.class, () -> this.orderService.closeOrder(0L));
    }
}

