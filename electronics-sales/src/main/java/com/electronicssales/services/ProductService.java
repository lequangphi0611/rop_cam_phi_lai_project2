package com.electronicssales.services;

import java.util.Collection;
import java.util.Optional;

import com.electronicssales.entities.Product;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.models.responses.ProductParameterResponse;

public interface ProductService {

    Product saveProduct(ProductDto productDto);

    Collection<ProductParameterResponse> getProductParametersByProductId(long productId);

    boolean existsById(long productId);

    boolean existsByProductName(String productName);

    Optional<Product> findByProductId(long id);
    
}