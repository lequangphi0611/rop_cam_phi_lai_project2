package com.electronicssales.repositories;

import java.util.List;

import com.electronicssales.entities.Product;
import com.electronicssales.models.responses.FetchProductOption;

public interface CustomizeProductRepository {

    List<Product> fetchProductsBy(FetchProductOption option);

    long countBy(FetchProductOption option);

    long countAll();
    
}