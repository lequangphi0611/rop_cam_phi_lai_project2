package com.electronicssales.services;

import java.util.Collection;

import com.electronicssales.entities.Product;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.models.responses.ProductParameterResponse;

public interface ProductService {

    Product saveProduct(ProductDto productDto);

    Collection<ProductParameterResponse> getProductParametersByProductId(long productId);
    
}