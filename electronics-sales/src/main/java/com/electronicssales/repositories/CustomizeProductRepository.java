package com.electronicssales.repositories;

import java.util.Collection;

import com.electronicssales.entities.Product;
import com.electronicssales.models.responses.FetchProductOption;

public interface CustomizeProductRepository {

    Collection<Product> fetchProductsBy(FetchProductOption option);
    
}