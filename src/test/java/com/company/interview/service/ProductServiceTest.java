package com.company.interview.service;

import com.company.interview.dto.ProductDto;
import com.company.interview.exception.badrequest.BadRequestException;
import com.company.interview.exception.product.ProductNotFoundException;
import com.company.interview.model.Order;
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

@ContextConfiguration(classes = {ProductService.class})
@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrderProductRepository orderProductRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Test
    public void Test_Constructor() {
        assertTrue((new ProductService(mock(OrderProductRepository.class), mock(ProductRepository.class),
                mock(OrderRepository.class))).getAllProducts().isEmpty());
    }

    @Test
    public void Test_GetAllProducts() {
        when(this.productRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(this.productService.getAllProducts().isEmpty());
        verify(this.productRepository).findAll();
    }

    @Test
    public void Test_AddProduct_1() {
        assertThrows(BadRequestException.class, () -> this.productService.addProduct(new ProductDto()));
    }

    @Test
    public void Test_AddProduct_2() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(42L));
        when(this.productRepository.save(any())).thenReturn(product);
        ProductDto productDto = mock(ProductDto.class);
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        when(productDto.getPrice()).thenReturn(valueOfResult);
        when(productDto.getName()).thenReturn("foo");
        Optional<Product> actualAddProductResult = this.productService.addProduct(productDto);
        assertTrue(actualAddProductResult.isPresent());
        Product getResult = actualAddProductResult.get();
        assertEquals("foo", getResult.getName());
        BigDecimal price = getResult.getPrice();
        assertSame(valueOfResult, price);
        assertEquals("42", price.toString());
        verify(this.productRepository).save(any());
        verify(productDto, times(2)).getName();
        verify(productDto, times(2)).getPrice();
        assertTrue(this.productService.getAllProducts().isEmpty());
    }

    @Test
    public void Test_AddProduct_3() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(42L));
        when(this.productRepository.save(any())).thenReturn(product);
        ProductDto productDto = mock(ProductDto.class);
        when(productDto.getPrice()).thenReturn(BigDecimal.valueOf(-1L));
        when(productDto.getName()).thenReturn("foo");
        assertThrows(BadRequestException.class, () -> this.productService.addProduct(productDto));
        verify(productDto).getName();
        verify(productDto).getPrice();
    }

    @Test
    public void Test_AddProduct_4() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(42L));
        when(this.productRepository.save(any())).thenReturn(product);
        ProductDto productDto = mock(ProductDto.class);
        when(productDto.getPrice()).thenReturn(null);
        when(productDto.getName()).thenReturn("foo");
        assertThrows(BadRequestException.class, () -> this.productService.addProduct(productDto));
        verify(productDto).getName();
        verify(productDto).getPrice();
    }

    @Test
    public void Test_UpdateProduct_1() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(42L));
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        assertThrows(BadRequestException.class, () -> this.productService.updateProduct(123L, new ProductDto()));
        verify(this.productRepository).findById(any());
    }

    @Test
    public void Test_UpdateProduct_2() {
        when(this.productRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> this.productService.updateProduct(123L, new ProductDto()));
        verify(this.productRepository).findById(any());
    }

    @Test
    public void Test_UpdateProduct_3() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(42L));
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        assertThrows(BadRequestException.class, () -> this.productService.updateProduct(0L, new ProductDto()));
    }

    @Test
    public void Test_UpdateProduct_4() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(42L));
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById(any())).thenReturn(ofResult);

        ProductDto productDto = new ProductDto();
        productDto.setName("Product");
        assertThrows(BadRequestException.class, () -> this.productService.updateProduct(123L, productDto));
        verify(this.productRepository).findById(any());
    }

    @Test
    public void Test_UpdateProduct_5() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(42L));
        Optional<Product> ofResult = Optional.of(product);

        Product product1 = new Product();
        product1.setProductId(123L);
        product1.setName("Product1");
        product1.setPrice(BigDecimal.valueOf(42L));
        when(this.productRepository.save(any())).thenReturn(product1);
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        when(this.orderRepository.findAll()).thenReturn(new ArrayList<>());

        ProductDto productDto = new ProductDto();
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        productDto.setPrice(valueOfResult);
        productDto.setName("Product");
        Product actualUpdateProductResult = this.productService.updateProduct(123L, productDto);
        assertSame(product, actualUpdateProductResult);
        assertEquals("Product", actualUpdateProductResult.getName());
        BigDecimal price = actualUpdateProductResult.getPrice();
        assertSame(valueOfResult, price);
        assertEquals("42", price.toString());
        verify(this.productRepository).findById(any());
        verify(this.productRepository).save(any());
        verify(this.orderRepository).findAll();
        assertTrue(this.productService.getAllProducts().isEmpty());
    }

    @Test
    public void Test_UpdateProduct_6() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(42L));
        Optional<Product> ofResult = Optional.of(product);

        Product product1 = new Product();
        product1.setProductId(123L);
        product1.setName("Product1");
        product1.setPrice(BigDecimal.valueOf(42L));
        when(this.productRepository.save(any())).thenReturn(product1);
        when(this.productRepository.findById(any())).thenReturn(ofResult);

        Order order = new Order();
        order.setModificationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setOrderProducts(new HashSet<>());
        order.setIsDone(true);
        order.setOrderDate(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setTotalPrice(0.0);
        order.setOrderId(123L);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(this.orderRepository.findAll()).thenReturn(orderList);

        ProductDto productDto = new ProductDto();
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        productDto.setPrice(valueOfResult);
        productDto.setName("Product");
        Product actualUpdateProductResult = this.productService.updateProduct(123L, productDto);
        assertSame(product, actualUpdateProductResult);
        assertEquals("Product", actualUpdateProductResult.getName());
        BigDecimal price = actualUpdateProductResult.getPrice();
        assertSame(valueOfResult, price);
        assertEquals("42", price.toString());
        verify(this.productRepository).findById(any());
        verify(this.productRepository).save(any());
        verify(this.orderRepository).findAll();
        assertTrue(this.productService.getAllProducts().isEmpty());
    }

    @Test
    public void Test_UpdateProduct_7() {
        Product product = new Product();
        product.setProductId(123L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(42L));
        Optional<Product> ofResult = Optional.of(product);

        Product product1 = new Product();
        product1.setProductId(123L);
        product1.setName("Product1");
        product1.setPrice(BigDecimal.valueOf(42L));
        when(this.productRepository.save(any())).thenReturn(product1);
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        when(this.orderRepository.findAll()).thenReturn(new ArrayList<>());

        ProductDto productDto = new ProductDto();
        productDto.setPrice(BigDecimal.valueOf(-1L));
        productDto.setName("Product");
        assertThrows(BadRequestException.class, () -> this.productService.updateProduct(123L, productDto));
        verify(this.productRepository).findById(any());
    }
}

