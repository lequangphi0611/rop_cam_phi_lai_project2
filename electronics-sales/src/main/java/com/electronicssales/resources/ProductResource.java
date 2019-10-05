package com.electronicssales.resources;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.electronicssales.entities.Product;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    private boolean validateProductBeforeCreate(ProductDto product) {
        if(productService.existsByProductName(product.getProductName())) {
            throw new EntityExistsException("Product is already exists !");
        }
        return true;
    }

    private boolean validateProductBeforeUpdate(ProductDto productCheckable) {
        Product productFinded = productService
            .findByProductId(productCheckable.getId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found !"));

        if(!productFinded.getProductName().equalsIgnoreCase(productCheckable.getProductName())
           && productService.existsByProductName(productCheckable.getProductName()) ) 
            throw new EntityExistsException("Product name is aleady exists !");

        return true;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto productDto) {
        this.validateProductBeforeCreate(productDto);

        return ResponseEntity
            .created(null)
            .body(productService.saveProduct(productDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
        @RequestBody @Valid ProductDto productDto, 
        @PathVariable("id") long productId
    ) 
    {
        validateProductBeforeUpdate(productDto);

        return ResponseEntity
            .created(null)
            .body(productService.saveProduct(productDto));
    }

    @GetMapping("/{id}/parameters")
    public ResponseEntity<?> fetchProductParameters(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProductParametersByProductId(id));
    }

}