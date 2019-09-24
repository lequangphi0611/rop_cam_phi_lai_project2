package com.electronicssales.resources;

import javax.validation.Valid;

import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto productDto) {
        return ResponseEntity
            .created(null)
            .body(productService.saveProduct(productDto));
    }

    @GetMapping("/{id}/parameters")
    public ResponseEntity<?> fetchProductParameters(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProductParametersByProductId(id));
    }
    
}