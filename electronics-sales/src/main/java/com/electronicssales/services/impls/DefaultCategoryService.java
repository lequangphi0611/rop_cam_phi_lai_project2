package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.ParameterType;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.responses.CategoryResponse;
import com.electronicssales.repositories.CategoryRepository;
import com.electronicssales.repositories.ParameterTypeRepository;
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
    private ParameterTypeRepository parameterTypeRepository;

    @Autowired
    private Mapper<Category, CategoryDto> categoryMapper;

    @Autowired
    private Mapper<CategoryResponse, Category> categoryResponseMapper;

    @Override
    public Category saveCategory(CategoryDto categoryDto) {

        List<ParameterType> parameterTypes = parameterTypeRepository
            .saveAll(categoryDto.getParameterTypes()
                .stream()
                .map((parameterType) -> {
                    if(!parameterTypeRepository.existsByParameterTypeName(parameterType.getParameterTypeName())){
                        return parameterType;
                    }
                    return  parameterTypeRepository
                        .findByParameterTypeName(parameterType.getParameterTypeName())
                        .get();
                })
                .collect(Collectors.toList()));
            
        return categoryRepository
            .save(categoryMapper.mapping(categoryDto));
    }

    @Override
    public Collection<Category> saveAll(Collection<CategoryDto> categoryDtos) {
        return categoryRepository
            .saveAll(
                categoryDtos
                    .stream()
                    .map(categoryMapper::mapping)
                    .collect(Collectors.toList())
            );
    }

    @Override
    public Collection<CategoryResponse> findAll() {
        return categoryRepository
            .findAll()
            .stream()
            .map(categoryResponseMapper::mapping)
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
            .map(categoryResponseMapper::mapping)
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
    class CategoryMapper implements Mapper<Category, CategoryDto> {

        @Override
        public Category mapping(CategoryDto categoryDto) {
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setCategoryName(categoryDto.getCategoryName());
            return category;
        }

    }

    @Component
    class CategoryResponseMapper implements Mapper<CategoryResponse, Category> {

        @Override
        public CategoryResponse mapping(Category category) {
            return new CategoryResponse(
                category.getId(), 
                category.getCategoryName(), 
                0
            );
        }
        
    }

    
}