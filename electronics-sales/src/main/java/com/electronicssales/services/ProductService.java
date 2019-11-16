package com.electronicssales.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.Product;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.models.responses.BaseCategoryResponse;
import com.electronicssales.models.responses.FetchProductOption;
import com.electronicssales.models.responses.ImageDataResponse;
import com.electronicssales.models.responses.ParagraphResponse;
import com.electronicssales.models.responses.ProductParameterResponse;
import com.electronicssales.models.responses.ProductResponse;

public interface ProductService {

    Product saveProduct(ProductDto productDto);

    List<ProductResponse> fetchProductsBy(FetchProductOption option);

    Collection<ProductParameterResponse> getProductParametersByProductId(long productId);

    ProductResponse createProduct(ProductDto productDto);

    ProductResponse updateProduct(ProductDto productDto);

    boolean existsById(long productId);

    boolean existsByProductName(String productName);

    Optional<Product> findByProductId(long id);

    Optional<Product> findByName(String productName);

    List<ImageDataResponse> getImages(long productId);

    void deleteById(long id);

    List<ParagraphResponse> getDescriptionsOf(long productId);

    List<BaseCategoryResponse> getCategoriesBy(long productId);

    long countProductBy(FetchProductOption option);
    
}