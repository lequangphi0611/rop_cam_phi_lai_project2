package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.stream.Collectors;

import com.electronicssales.entities.Category;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.responses.CategoryResponse;
import com.electronicssales.repositories.CategoryRepository;
import com.electronicssales.services.CategoryService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class DefaultCategoryService implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Mapper<Category, CategoryDto> categoryMapper;

    @Autowired
    private Mapper<CategoryResponse, Category> categoryResponseMapper;

    @Override
    public Category saveCategory(CategoryDto categoryDto) {
        return categoryRepository
            .save(categoryMapper.map(categoryDto));
    }

    @Override
    public Collection<Category> saveAll(Collection<CategoryDto> categoryDtos) {
        return categoryRepository
            .saveAll(
                categoryDtos
                    .stream()
                    .map(categoryMapper::map)
                    .collect(Collectors.toList())
            );
    }

    @Override
    public Collection<CategoryResponse> findAll() {
        return categoryRepository
            .findAll()
            .stream()
            .map(categoryResponseMapper::map)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Collection<CategoryResponse> findByCategoryNameContaining(String query) {
        return categoryRepository
            .findByCategoryNameContaining(query)
            .stream()
            .map(categoryResponseMapper::map)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Category> findByCategoryNameContainingAndPageable(String categoryName, Pageable pageable) {
        return categoryRepository.findByCategoryNameContaining(categoryName, pageable);
    }

    @Override
    public boolean existsByCategoryName(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }

    @Override
    public boolean existsById(long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public void deleteCategoryById(long id) {
        categoryRepository.deleteById(id);
    }

    @Component
    static class CategoryMapper implements Mapper<Category, CategoryDto> {

        @Override
        public Category map(CategoryDto categoryDto) {
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setCategoryName(categoryDto.getCategoryName());
            return category;
        }

    }

    @Component
    static class CategoryResponseMapper implements Mapper<CategoryResponse, Category> {

        @Override
        public CategoryResponse map(Category category) {
            return new CategoryResponse(
                category.getId(), 
                category.getCategoryName(), 
                0
            );
        }
        
    }

    
}