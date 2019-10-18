package com.electronicssales.repositories;

import java.util.List;

import com.electronicssales.entities.Product;
import com.electronicssales.models.responses.FetchProductOption;

import org.springframework.data.domain.Page;

public interface CustomizeProductRepository {

    Page<Product> fetchProductsBy(FetchProductOption option);

    long countAll();

    List<Long> findCategoryIdsByProductId(long productId);

    List<Long> findImageIdsByProductId(long productId);
    
}