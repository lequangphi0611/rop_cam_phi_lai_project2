package com.electronicssales.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.ParameterType;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.dtos.ManufacturerDto;
import com.electronicssales.models.responses.CategoryResponse;

public interface CategoryService {

    Category saveCategory(CategoryDto categoryDto);

    Category createCategory(CategoryDto categoryDto);

    Category updateCategory(CategoryDto categoryDto);

    Optional<Category> findByCategoryName(String categoryName);

    Collection<Category> saveAll(Collection<CategoryDto> categoryDtos);
    
    Collection<CategoryResponse> findAll(String nameKeyword);

    Optional<Category> findById(long id);

    boolean existsByCategoryName(String categoryName);

    boolean existsById(long id);

    void deleteCategoryById(long id);

    List<CategoryResponse> fetchChildrensOf(long parentId, String nameQuery);

    List<ParameterType> fetchParameterTypeByCategoryId(long categoryId);

    List<ManufacturerDto> fetchManufacturersByCategoryId(long categoryId);

}