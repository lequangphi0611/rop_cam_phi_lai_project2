package com.electronicssales.services;

import java.util.Collection;

import com.electronicssales.entities.Category;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.responses.CategoryResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Category saveCategory(CategoryDto category);

    Collection<Category> saveAll(Collection<CategoryDto> categoryDtos);
    
    Collection<CategoryResponse> findAll();

    boolean existsByCategoryName(String categoryName);

    Page<Category> findAll(Pageable pageable);

    Page<Category> findByCategoryNameContainingAndPageable(String query, Pageable pageable);

    Collection<CategoryResponse> findByCategoryNameContaining(String query);

    boolean existsById(long id);

    void deleteCategoryById(long id);

}