package com.electronicssales.repositories;

import com.electronicssales.entities.Product;
import com.electronicssales.models.responses.FetchProductOption;

import org.springframework.data.domain.Page;

public interface CustomizeProductRepository {

    Page<Product> fetchProductsBy(FetchProductOption option);

    long countAll();
    
}