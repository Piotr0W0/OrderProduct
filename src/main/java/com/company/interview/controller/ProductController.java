package com.company.interview.controller;

import com.company.interview.dto.ProductDto;
import com.company.interview.model.Product;
import com.company.interview.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        Optional<Product> optionalProduct = productService.addProduct(productDto);
        return optionalProduct.<ResponseEntity<?>>map(product -> new ResponseEntity<>("Product " + product.getProductId() + " was created", HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>("Bad format date", HttpStatus.BAD_REQUEST));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
                                           @RequestBody ProductDto productDto) {
        Product product = productService.updateProduct(productId, productDto);
        return new ResponseEntity<>("Product " + product.getProductId() + " was updated", HttpStatus.OK);
    }
}
