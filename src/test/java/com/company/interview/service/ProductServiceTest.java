package com.company.interview.service;

import com.company.interview.dto.ProductDto;
import com.company.interview.exception.product.ProductNotFoundException;
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
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    public void Should_Get_All_Products() {
        when(this.productRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(this.productService.getAllProducts().isEmpty());
        verify(this.productRepository).findAll();
    }

    @Test
    public void Should_Post_Empty_Product() {
        assertFalse(this.productService.postProduct(new ProductDto()).isPresent());
    }

    @Test
    public void Should_Not_Post_Product_With_Invalid_Attributes() {
        ProductDto productDto = mock(ProductDto.class);
        when(productDto.hasInvalidAttributes()).thenReturn(true);
        assertFalse(this.productService.postProduct(productDto).isPresent());
        verify(productDto).hasInvalidAttributes();
        assertTrue(this.productService.getAllProducts().isEmpty());
    }

    @Test
    public void Should_Post_Product() {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(10L));
        when(this.productRepository.save(any())).thenReturn(product);
        ProductDto productDto = mock(ProductDto.class);
        BigDecimal valueOfResult = BigDecimal.valueOf(10L);
        when(productDto.getPrice()).thenReturn(valueOfResult);
        when(productDto.getName()).thenReturn("something");
        when(productDto.hasInvalidAttributes()).thenReturn(false);
        Optional<Product> actualPostProductResult = this.productService.postProduct(productDto);
        assertTrue(actualPostProductResult.isPresent());
        Product getResult = actualPostProductResult.get();
        assertEquals("something", getResult.getName());
        BigDecimal price = getResult.getPrice();
        assertSame(valueOfResult, price);
        assertEquals("10", price.toString());
        verify(this.productRepository).save(any());
        verify(productDto).getName();
        verify(productDto).getPrice();
        verify(productDto).hasInvalidAttributes();
        assertTrue(this.productService.getAllProducts().isEmpty());
    }

    @Test
    public void Should_Throw_ProductNotFoundException_When_Can_Not_Find_Product() {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(10L));
        Optional<Product> ofResult = Optional.of(product);

        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Product");
        product1.setPrice(BigDecimal.valueOf(10L));
        when(this.productRepository.save(any())).thenReturn(product1);
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        when(this.orderRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(assertThrows(ProductNotFoundException.class,
                () -> this.productService.putProduct(1L, new ProductDto()),
                "Could not find product")
                .getMessage()
                .contains("Could not find product"));
    }

    @Test
    public void Should_Throw_Exception() {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(10L));
        when(this.productRepository.save(any())).thenReturn(product);
        when(this.productRepository.findById(any())).thenReturn(Optional.empty());
        when(this.orderRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(ProductNotFoundException.class, () -> this.productService.putProduct(1L, new ProductDto()));
        verify(this.productRepository).findById(any());
    }

    @Test
    public void Should_Throw_ProductNotFoundException_When_productId_Is_Null() {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(10L));
        Optional<Product> ofResult = Optional.of(product);

        Product product1 = new Product();
        product1.setProductId(123L);
        product1.setName("Product");
        product1.setPrice(BigDecimal.valueOf(10L));
        when(this.productRepository.save(any())).thenReturn(product1);
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        when(this.orderRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(ProductNotFoundException.class, () -> this.productService.putProduct(null, new ProductDto()));
    }

    @Test
    public void Should_Throw_ProductNotFoundException_When_productId_Is_Negative() {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(10L));
        Optional<Product> ofResult = Optional.of(product);

        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Product");
        product1.setPrice(BigDecimal.valueOf(10L));
        when(this.productRepository.save(any())).thenReturn(product1);
        when(this.productRepository.findById(any())).thenReturn(ofResult);
        when(this.orderRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(ProductNotFoundException.class, () -> this.productService.putProduct(-1L, new ProductDto()));
    }
}

