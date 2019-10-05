package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.List;
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
import com.electronicssales.repositories.ProductCategoryRepository;
import com.electronicssales.services.CategoryService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class DefaultCategoryService implements CategoryService {

    @Lazy
    @Autowired
    private CategoryRepository categoryRepository;

    @Lazy
    @Autowired
    private ParameterTypeRepository parameterTypeRepository;

    @Lazy
    @Autowired
    private CategoryParameterTypeRepository categoryParameterTypeRepository;

    @Lazy
    @Autowired
    private Mapper<Category, CategoryDto> categoryMapper;

    @Lazy
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

    private Collection<CategoryParameterType> saveCategoryParameterTypes(Collection<ParameterType> parameterTypes, Category categorySaved) {
        return parameterTypes
            .stream()
            .map(this::saveParameterType)
            .map(this.getCategoryParameterTypeMapper(categorySaved))
            .map(this::saveCategoryParameterType)
            .collect(Collectors.toList());
    }

    private Collection<Category> saveChildrens(Collection<CategoryDto> childrensDtos) {
        return childrensDtos
            .stream()
            .map(this::saveCategory)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Category saveCategory(CategoryDto categoryDto) {
        Category categorySaved = categoryRepository.save(categoryMapper.mapping(categoryDto));

        saveCategoryParameterTypes(categoryDto.getParameterTypes(), categorySaved);

        saveChildrens(categoryDto
            .getChildrens()
            .stream()
            .peek(children -> children.setParentId(categorySaved.getId()))
            .collect(Collectors.toList())
        );

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
    @Transactional
    public List<CategoryResponse> fetchChildrensOf(long parentId) {
        return categoryRepository
            .findByParentId(parentId)
            .stream()
            .map(categoryResponseMapper::mapping)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Collection<CategoryResponse> findByCategoryNameContaining(String query) {
        return categoryRepository
            .findByCategoryNameContaining(query)
            .stream()
            .map(categoryResponseMapper::mapping)
            .collect(Collectors.toList());
    }

    
    @Override
    @Transactional
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

    @Override
    public List<ParameterType> fetchParameterTypeByCategoryId(long categoryId) {
        return categoryParameterTypeRepository
            .findAllByCategoryId(categoryId)
            .stream()
            .map(categoryParameter -> categoryParameter.getParameterType())
            .collect(Collectors.toList());
    }

    @Lazy
    @Component
    class CategoryMapper implements Mapper<Category, CategoryDto> {

        @Override
        public Category mapping(CategoryDto categoryDto) {
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setCategoryName(categoryDto.getCategoryName());
            if(categoryDto.getParentId() > 0) {
                category.setParent(new Category(categoryDto.getParentId()));
            }
            return category;
        }

    }

    @Lazy
    @Component
    class CategoryResponseMapper implements Mapper<CategoryResponse, Category> {

        @Lazy
        @Autowired
        private ProductCategoryRepository productCategoryRepository;

        @Override
        public CategoryResponse mapping(Category category) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(category.getId());
            categoryResponse.setCategoryName(category.getCategoryName());
            categoryResponse.setProductCount(productCategoryRepository.countByCategoryId(category.getId()));
            categoryResponse.setParentId(category.getParent() != null ? category.getParent().getId() : 0);
            return categoryResponse;
        }
        
    }

    
}