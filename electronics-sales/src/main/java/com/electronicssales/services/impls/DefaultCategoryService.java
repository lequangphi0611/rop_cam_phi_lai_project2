package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.CategoryParameterType;
import com.electronicssales.entities.ParameterType;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.responses.CategoryResponse;
import com.electronicssales.repositories.CategoryParameterTypeRepository;
import com.electronicssales.repositories.CategoryRepository;
import com.electronicssales.repositories.ParameterTypeRepository;
import com.electronicssales.repositories.ProductRepository;
import com.electronicssales.services.CategoryService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultCategoryService implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ParameterTypeRepository parameterTypeRepository;

    @Autowired
    private CategoryParameterTypeRepository categoryParameterTypeRepository;

    @Autowired
    private Mapper<Category, CategoryDto> categoryMapper;

    @Autowired
    private Mapper<CategoryResponse, Category> categoryResponseMapper;

    private Function<ParameterType, CategoryParameterType> getCategoryParameterTypeMapper(Category category) {
        return (parameterType) -> {
            CategoryParameterType categoryParameterType = new CategoryParameterType();
            categoryParameterType.setCategory(category);
            categoryParameterType.setParameterType(new ParameterType(parameterType.getId()));
            return categoryParameterType;
        };
    }

    private ParameterType saveParameterType(ParameterType parameterType) {
        return parameterTypeRepository
            .findByParameterTypeName(parameterType.getParameterTypeName())
            .orElseGet(() -> parameterTypeRepository.save(parameterType));
    }

    private CategoryParameterType saveCategoryParameterType(CategoryParameterType categoryParameterType) {
        final long categoryId = categoryParameterType.getCategory().getId();
        final long parameterTypeId = categoryParameterType.getParameterType().getId();
        return categoryParameterTypeRepository
            .findByCategoryIdAndParameterTypeId(categoryId, parameterTypeId)
            .orElseGet(() -> categoryParameterTypeRepository.save(categoryParameterType));
    }

    private Collection<CategoryParameterType> processAfterSaveCategory(Collection<ParameterType> parameterTypes, Category categorySaved) {
        return parameterTypes
            .stream()
            .map(this::saveParameterType)
            .map(this.getCategoryParameterTypeMapper(categorySaved))
            .map(this::saveCategoryParameterType)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Category saveCategory(CategoryDto categoryDto) {
        Category categorySaved = categoryRepository.save(categoryMapper.mapping(categoryDto));
        processAfterSaveCategory(categoryDto.getParameterTypes(), categorySaved);
        return categorySaved;
    }

    @Override
    public Collection<Category> saveAll(Collection<CategoryDto> categoryDtos) {
        return categoryDtos
            .stream()
            .map(this::saveCategory)
            .collect(Collectors.toList());
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

        @Autowired
        private ProductRepository productRepository;

        @Override
        public CategoryResponse mapping(Category category) {
            return new CategoryResponse(
                category.getId(), 
                category.getCategoryName(), 
                productRepository.countByCategoryId(category.getId())
            );
        }
        
    }

    
}