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
import com.electronicssales.models.responses.CategoryResponse;
import com.electronicssales.models.responses.ICategoryReponse;
import com.electronicssales.repositories.CategoryManufacturerRepository;
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
    private CategoryManufacturerRepository categoryManufacturerRepository;

    @Lazy
    @Autowired
    private ParameterTypeRepository parameterTypeRepository;

    @Lazy
    @Autowired
    private Mapper<ManufacturerDto, Manufacturer> manufacturerDtoMapper;

    @Lazy
    @Autowired
    private Mapper<CategoryResponse, ICategoryReponse> categoryResponseMapper;

    private Collection<CategoryManufacturer> createCategoryManufacturers(Collection<CategoryManufacturer> categoryManufacturers) {
        return categoryManufacturers
            .stream()
            .map(categoryManufacturerRepository::persist)
            .collect(Collectors.toList());
    };

    @Transactional
    private boolean isNew(CategoryDto category) {
        return !categoryRepository.existsById(category.getId())
            && !categoryRepository.existsByCategoryName(category.getCategoryName());
    }

    private Collection<CategoryManufacturer> createCategoryManufacturers(Category category ,Collection<Long> manufacturerIds) {
        Collection<CategoryManufacturer> categoryManufacturers = manufacturerIds
            .stream()
            .map(manufacturerId -> new CategoryManufacturer(
                    category, 
                    new Manufacturer(manufacturerId)
            ))
            .collect(Collectors.toList());

        return createCategoryManufacturers(categoryManufacturers);
    }

    @Transactional
    @Override
    public Category saveCategory(CategoryDto categoryDto) {
        if(isNew(categoryDto)) {
            return createCategory(categoryDto);
        }
        return updateCategory(categoryDto);
    }

    @Transactional
    private void saveCategoryParameters(Category category, Collection<ParameterType> parameterTypes) {
        parameterTypes
            .stream()   
            .map(parameterTypeRepository::save)
            .peek(parameterType -> categoryRepository.saveCategoryParameter(category, parameterType))
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Category createCategory(CategoryDto categoryDto) {
        Category categoryTransient = categoryMapper.mapping(categoryDto);
        Category categoryPersisted = categoryRepository.persist(categoryTransient);

        saveCategoryParameters(categoryPersisted, categoryDto.getParameterTypes());

        createCategoryManufacturers(categoryPersisted, categoryDto.getManufacturersId());

        return categoryPersisted;
    }

    @Transactional
    @Override
    public Category updateCategory(CategoryDto categoryDto) {
        
        Category categoryTransient = categoryMapper.mapping(categoryDto);

        // Delete all CategoryParameters
        categoryRepository.deleteCategoryParametersByCategoryId(categoryTransient.getId());

        // Delete all CategoryManufacturers
        categoryManufacturerRepository.deleteByCategoryId(categoryTransient.getId());

        // Save CategoryParameters
        saveCategoryParameters(categoryTransient, categoryDto.getParameterTypes());

        // Save CategoryManufacturers
        createCategoryManufacturers(categoryTransient, categoryDto.getManufacturersId());

        // Update Category
        categoryRepository.merge(categoryTransient);
        return categoryTransient;
    }

    @Transactional
    @Override
    public Collection<Category> saveAll(Collection<CategoryDto> categoryDtos) {
        return categoryDtos
            .stream()
            .map(this::saveCategory)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Collection<CategoryResponse> findAll(String nameKeyword) {
        return categoryRepository
            .fetchCategoriesNotHasParent(nameKeyword)
            .stream()
            .map(categoryResponseMapper::mapping)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryResponse> fetchChildrensOf(long parentId, String nameQuery) {
        return categoryRepository
            .fetchChildrensOf(parentId, nameQuery)
            .stream()
            .map(categoryResponseMapper::mapping)
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

    @Transactional
    @Override
    public List<ManufacturerDto> fetchManufacturersByCategoryId(long categoryId) {
        return categoryManufacturerRepository
            .fetchCategoryManufacturersByCategoryId(categoryId)
            .stream()
            .map(categorymanufacturer -> manufacturerDtoMapper
                .mapping(categorymanufacturer.getManufacturer()))
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
            Collection<CategoryResponse> childrens = !categoryRepository.hasChildrens(iCategory.getId())
                ? null 
                : categoryRepository
                    .fetchChildrensOf(iCategory.getId(), "")
                    .stream()
                    .map(this::mapping)
                    .collect(Collectors.toList());

            categoryResponse.setChildrens(childrens);

            return categoryResponse;
        }
        
    }
    
}