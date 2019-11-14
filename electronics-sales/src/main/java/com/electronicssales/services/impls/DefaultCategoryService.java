package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.CategoryManufacturer;
import com.electronicssales.entities.Manufacturer;
import com.electronicssales.entities.ParameterType;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.dtos.ManufacturerDto;
import com.electronicssales.models.responses.BaseCategoryResponse;
import com.electronicssales.models.responses.CategoryResponse;
import com.electronicssales.models.responses.ICategoryReponse;
import com.electronicssales.repositories.CategoryRepository;
import com.electronicssales.repositories.ParameterTypeRepository;
import com.electronicssales.services.CategoryService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private Mapper<Category, CategoryDto> categoryMapper;

    @Lazy
    @Autowired
    private ParameterTypeRepository parameterTypeRepository;

    @Lazy
    @Autowired
    private Mapper<ManufacturerDto, Manufacturer> manufacturerDtoMapper;

    @Lazy
    @Autowired
    private Mapper<CategoryResponse, ICategoryReponse> categoryResponseMapper;

    @Lazy
    @Autowired
    private Mapper<BaseCategoryResponse, Category> baseCategoryResponseMapper;

    @Transactional
    @Override
    public BaseCategoryResponse createCategory(CategoryDto categoryDto) {
        return Optional.of(categoryDto).map(categoryMapper::mapping).map(categoryRepository::save)
                .map(baseCategoryResponseMapper::mapping).get();
    }

    @Transactional
    @Override
    public BaseCategoryResponse updateCategory(CategoryDto categoryDto) {
        return Optional.of(categoryDto).map(categoryMapper::mapping).map(categoryRepository::save)
                .map(baseCategoryResponseMapper::mapping).get();
    }

    @Transactional
    @Override
    public Collection<CategoryResponse> findAll(String nameKeyword) {
        return categoryRepository.fetchCategoriesNotHasParent(nameKeyword).stream().map(categoryResponseMapper::mapping)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryResponse> fetchChildrensOf(long parentId, String nameQuery) {
        return categoryRepository.fetchChildrensOf(parentId, nameQuery).stream().map(categoryResponseMapper::mapping)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    @Transactional
    @Override
    public Optional<Category> findById(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public boolean existsByCategoryName(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }

    @Transactional
    @Override
    public boolean existsById(long id) {
        return categoryRepository.existsById(id);
    }

    @Transactional
    @Override
    public void deleteCategoryById(long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<ParameterType> fetchParameterTypeByCategoryId(long categoryId) {
        return parameterTypeRepository.findByCategoriesId(categoryId);
    }

    @Lazy
    @Component
    class CategoryMapper implements Mapper<Category, CategoryDto> {

        @Autowired
        private ParameterTypeRepository parameterTypeRepository;

        @Override
        public Category mapping(CategoryDto categoryDto) {
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setCategoryName(categoryDto.getCategoryName());
            Optional.ofNullable(categoryDto.getParentId())
                    .ifPresent(parentId -> category.setParent(Category.of(parentId)));
            Optional.ofNullable(categoryDto.getParameterTypes()).ifPresent(parameterTypes -> {
                List<ParameterType> newParameterTypes = parameterTypes.stream()
                        .map(parameterType -> parameterTypeRepository
                                .findByParameterTypeName(parameterType.getParameterTypeName()).orElse(parameterType))
                        .collect(Collectors.toList());
                category.setParameterTypes(newParameterTypes);
            });
            Optional.ofNullable(categoryDto.getManufacturerIds()).ifPresent(manufacturerIds -> {
                List<CategoryManufacturer> categoryManufacturers = manufacturerIds.stream()
                        .map(manufacturerId -> CategoryManufacturer.of(category, Manufacturer.of(manufacturerId)))
                        .collect(Collectors.toList());
                category.setCategoryManufacturers(categoryManufacturers);
            });
            return category;
        }
    }

    @Lazy
    @Component
    class BaseCategoryMapper implements Mapper<BaseCategoryResponse, Category> {

        @Override
        public BaseCategoryResponse mapping(Category category) {
            BaseCategoryResponse baseCategoryResponse = new BaseCategoryResponse();
            baseCategoryResponse.setId(category.getId());
            baseCategoryResponse.setCategoryName(category.getCategoryName());
            Optional.ofNullable(category.getParent())
                    .ifPresent(parent -> baseCategoryResponse.setParentId(parent.getId()));
            return baseCategoryResponse;
        }

    }

    @Lazy
    @Component
    class CategoryResponseMapper implements Mapper<CategoryResponse, ICategoryReponse> {

        @Lazy
        @Autowired
        private CategoryRepository categoryRepository;

        @Override
        public CategoryResponse mapping(ICategoryReponse iCategory) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(iCategory.getId());
            categoryResponse.setCategoryName(iCategory.getCategoryName());
            categoryResponse.setProductCount(iCategory.getProductCount());
            categoryResponse.setParentId(iCategory.getParentId());
            List<CategoryResponse> childrens = !categoryRepository.hasChildrens(iCategory.getId()) ? null
                    : categoryRepository.fetchChildrensOf(iCategory.getId(), "").stream().map(this::mapping)
                            .collect(Collectors.toList());

            categoryResponse.setChildrens(childrens);

            return categoryResponse;
        }

    }

}