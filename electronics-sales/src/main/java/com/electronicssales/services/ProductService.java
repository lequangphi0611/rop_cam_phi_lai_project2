package com.electronicssales.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.Product;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.models.responses.FetchProductOption;
import com.electronicssales.models.responses.ParagraphResponse;
import com.electronicssales.models.responses.ProductParameterResponse;

import org.springframework.data.domain.Page;

public interface ProductService {

    Product saveProduct(ProductDto productDto);

    Page<Product> fetchProductsBy(FetchProductOption option);

    Collection<ProductParameterResponse> getProductParametersByProductId(long productId);

    Product createProduct(ProductDto productDto);

    Product updateProduct(ProductDto productDto);

    boolean existsById(long productId);

    boolean existsByProductName(String productName);

    Optional<Product> findByProductId(long id);

    Optional<Product> findByName(String productName);

    void deleteById(long id);

    List<ParagraphResponse> getDescriptionsOf(long productId);
    
}