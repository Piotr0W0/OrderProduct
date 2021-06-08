package com.company.interview.service;

import com.company.interview.dto.OrderPeriodDto;
import com.company.interview.exception.badrequest.BadRequestException;
import com.company.interview.exception.product.ProductNotFoundException;
import com.company.interview.model.Order;
import com.company.interview.model.OrderProduct;
import com.company.interview.model.Product;
import com.company.interview.repository.OrderProductRepository;
import com.company.interview.repository.OrderRepository;
import com.company.interview.repository.ProductRepository;
import org.apache.logging.log4j.message.ReusableSimpleMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(SpringExtension.class)
public class OrderServiceTest {
    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderProductRepository orderProductRepository;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @Test
    public void Should_Get_All_Orders() {
        when(this.orderRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertTrue(this.orderService.getAllOrders().isEmpty());
        verify(this.orderRepository).findAll();
    }

    @Test
    public void Should_Not_Return_Any_Orders_When_Period_Is_Empty() {
        assertTrue(this.orderService.getOrdersFromPeriod(new OrderPeriodDto()).isEmpty());
    }

    @Test
    public void HasInvalidAttributes_Test() {
        OrderPeriodDto orderPeriodDto = mock(OrderPeriodDto.class);
        when(orderPeriodDto.hasInvalidAttributes()).thenReturn(true);
        assertTrue(this.orderService.getOrdersFromPeriod(orderPeriodDto).isEmpty());
        verify(orderPeriodDto).hasInvalidAttributes();
        assertTrue(this.orderService.getAllOrders().isEmpty());
    }

    @Test
    public void BadRequestException_Test() {
        OrderPeriodDto orderPeriodDto = mock(OrderPeriodDto.class);
        when(orderPeriodDto.getStartDate()).thenReturn("something");
        when(orderPeriodDto.hasInvalidAttributes()).thenReturn(false);
        assertThrows(BadRequestException.class, () -> this.orderService.getOrdersFromPeriod(orderPeriodDto));
        verify(orderPeriodDto).getStartDate();
        verify(orderPeriodDto).hasInvalidAttributes();
    }

    @Test
    public void Format_Date_Exception_Test() {
        OrderPeriodDto orderPeriodDto = mock(OrderPeriodDto.class);
        when(orderPeriodDto.getStartDate())
                .thenThrow(new DateTimeParseException("An error occurred", new ReusableSimpleMessage(), -1));
        when(orderPeriodDto.hasInvalidAttributes()).thenReturn(false);
        assertThrows(BadRequestException.class, () -> this.orderService.getOrdersFromPeriod(orderPeriodDto));
        verify(orderPeriodDto).getStartDate();
        verify(orderPeriodDto).hasInvalidAttributes();
    }

    @Test
    public void Order_Test() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(1L);
        when(this.orderRepository.save(any())).thenReturn(order);
        Order actualOpenOrderResult = this.orderService.openOrder();
        assertFalse(actualOpenOrderResult.getIsDone());
        assertEquals(0.0, actualOpenOrderResult.getTotalPrice().doubleValue());
        assertTrue(actualOpenOrderResult.getOrderProducts().isEmpty());
        verify(this.orderRepository).save(any());
    }

    @Test
    public void Multiple_Order_Test() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(1L);
        Optional<Order> ofResult = Optional.of(order);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(new HashSet<>());
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(10.0);
        order1.setOrderId(1L);
        when(this.orderRepository.save(any())).thenReturn(order1);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        assertSame(order, this.orderService.calculateOrder(1L));
        verify(this.orderRepository).findById(any());
        verify(this.orderRepository).save(any());
    }

    @Test
    public void Second_Multiple_Order_Test() {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(10L));
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById(any())).thenReturn(ofResult);

        Order order = new Order();
        order.setModificationDate(null);
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(null);
        order.setTotalPrice(null);
        order.setOrderId(1L);

        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName(null);
        product1.setPrice(null);

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderProductId(1L);
        orderProduct.setQuantity(0L);
        orderProduct.setPrice(null);
        orderProduct.setOrder(order);
        orderProduct.setProduct(product1);

        HashSet<OrderProduct> orderProductSet = new HashSet<>();
        orderProductSet.add(orderProduct);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(orderProductSet);
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(10.0);
        order1.setOrderId(1L);
        Optional<Order> ofResult1 = Optional.of(order1);

        Order order2 = new Order();
        order2.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order2.setOrderProducts(new HashSet<OrderProduct>());
        order2.setIsDone(true);
        order2.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order2.setTotalPrice(10.0);
        order2.setOrderId(1L);
        when(this.orderRepository.save(any())).thenReturn(order2);
        when(this.orderRepository.findById(any())).thenReturn(ofResult1);

        Order order3 = new Order();
        order3.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order3.setOrderProducts(new HashSet<OrderProduct>());
        order3.setIsDone(true);
        order3.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order3.setTotalPrice(10.0);
        order3.setOrderId(1L);

        Product product2 = new Product();
        product2.setProductId(1L);
        product2.setName("Product");
        product2.setPrice(BigDecimal.valueOf(10L));

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setOrderProductId(1L);
        orderProduct1.setQuantity(1L);
        orderProduct1.setPrice(BigDecimal.valueOf(10L));
        orderProduct1.setOrder(order3);
        orderProduct1.setProduct(product2);
        when(this.orderProductRepository.save(any())).thenReturn(orderProduct1);
        Order actualCalculateOrderResult = this.orderService.calculateOrder(1L);
        assertSame(order1, actualCalculateOrderResult);
        assertEquals(0.0, actualCalculateOrderResult.getTotalPrice().doubleValue());
        verify(this.productRepository).findById(any());
        verify(this.orderRepository).findById(any());
        verify(this.orderRepository, times(2)).save(any());
        verify(this.orderProductRepository).save(any());
    }

    @Test
    public void Third_Multiple_Order_Test() {
        when(this.productRepository.findById(any())).thenReturn(Optional.empty());

        Order order = new Order();
        order.setModificationDate(null);
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(null);
        order.setTotalPrice(null);
        order.setOrderId(1L);

        Product product = new Product();
        product.setProductId(1L);
        product.setName(null);
        product.setPrice(null);

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderProductId(1L);
        orderProduct.setQuantity(0L);
        orderProduct.setPrice(null);
        orderProduct.setOrder(order);
        orderProduct.setProduct(product);

        HashSet<OrderProduct> orderProductSet = new HashSet<OrderProduct>();
        orderProductSet.add(orderProduct);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(orderProductSet);
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(10.0);
        order1.setOrderId(1L);
        Optional<Order> ofResult = Optional.of(order1);

        Order order2 = new Order();
        order2.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order2.setOrderProducts(new HashSet<OrderProduct>());
        order2.setIsDone(true);
        order2.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order2.setTotalPrice(10.0);
        order2.setOrderId(1L);
        when(this.orderRepository.save(any())).thenReturn(order2);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        assertThrows(ProductNotFoundException.class, () -> this.orderService.calculateOrder(123L));
        verify(this.productRepository).findById(any());
        verify(this.orderRepository).findById(any());
    }

    @Test
    public void Fourth_Multiple_Order_Test() {
        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(10.0);
        order.setOrderId(1L);
        Optional<Order> ofResult = Optional.of(order);

        Order order1 = new Order();
        order1.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setOrderProducts(new HashSet<>());
        order1.setIsDone(true);
        order1.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order1.setTotalPrice(10.0);
        order1.setOrderId(1L);
        when(this.orderRepository.save(any())).thenReturn(order1);
        when(this.orderRepository.findById(any())).thenReturn(ofResult);
        Order actualCloseOrderResult = this.orderService.closeOrder(1L);
        assertSame(order, actualCloseOrderResult);
        assertTrue(actualCloseOrderResult.getIsDone());
        verify(this.orderRepository).findById(any());
        verify(this.orderRepository).save(any());
    }
}

