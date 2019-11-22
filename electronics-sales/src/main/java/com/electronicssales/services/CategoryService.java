package com.electronicssales.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.ParameterType;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.responses.BaseCategoryResponse;
import com.electronicssales.models.responses.CategoryResponse;

import org.springframework.data.domain.Pageable;

public interface CategoryService {

    BaseCategoryResponse createCategory(CategoryDto categoryDto);

    BaseCategoryResponse updateCategory(CategoryDto categoryDto);

    Optional<Category> findByCategoryName(String categoryName);
    
    Collection<CategoryResponse> findAllAndFetchChildrens(String nameKeyword);

    Collection<BaseCategoryResponse> findAll(Pageable pageable, String nameKeyword);

    Collection<BaseCategoryResponse> findAll(String nameKeyword);

    List<CategoryResponse> fetchCategoriesHasProductSellable();

    Optional<Category> findById(long id);

    boolean existsByCategoryName(String categoryName);

    boolean existsById(long id);

    void deleteCategoryById(long id);

    List<CategoryResponse> fetchChildrensOf(long parentId, String nameQuery);

    List<ParameterType> fetchParameterTypeByCategoryId(long categoryId);

}