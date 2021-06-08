package com.company.interview.controller;

import com.company.interview.dto.ProductDto;
import com.company.interview.model.Product;
import com.company.interview.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> postProduct(@RequestBody ProductDto productDto) {
        Optional<Product> optionalProduct = productService.postProduct(productDto);
        if (optionalProduct.isPresent()) {
            return new ResponseEntity<>("Product was created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> putProduct(@PathVariable Long productId,
                                        @RequestBody ProductDto productDto) {
        Product product = productService.putProduct(productId, productDto);
        return new ResponseEntity<>("Product " + product.getProductId() + " was updated", HttpStatus.OK);
    }
}
