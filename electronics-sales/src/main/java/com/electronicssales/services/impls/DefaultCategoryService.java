package com.electronicssales.services.impls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.CategoryManufacturer;
import com.electronicssales.entities.Manufacturer;
import com.electronicssales.entities.ParameterType;
import com.electronicssales.models.CategoryIdAndNameOnly;
import com.electronicssales.models.CategoryProjections;
import com.electronicssales.models.ProductNameAndIdOnly;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.dtos.ManufacturerDto;
import com.electronicssales.models.responses.BaseCategoryResponse;
import com.electronicssales.models.responses.CategoryResponse;
import com.electronicssales.models.responses.ICategoryReponse;
import com.electronicssales.repositories.CategoryRepository;
import com.electronicssales.repositories.ParameterTypeRepository;
import com.electronicssales.repositories.ProductCategoryRepository;
import com.electronicssales.repositories.ProductRepository;
import com.electronicssales.services.CategoryService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    private ProductRepository productRepository;

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
    public Collection<CategoryResponse> findAllAndFetchChildrens(String nameKeyword) {
        List<CategoryResponse> categoryResponses = categoryRepository.fetchCategoriesNotHasParent(nameKeyword).stream()
                .map(categoryResponseMapper::mapping).collect(Collectors.toList());

        return groupingCategoryResponse(categoryResponses);
    }

    @Override
    public Collection<BaseCategoryResponse> findAll(Pageable pageable, String nameKeyword) {
        String keyword = Optional.ofNullable(nameKeyword).orElse("");
        return categoryRepository.findAll(pageable, keyword).stream().map(baseCategoryResponseMapper::mapping)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BaseCategoryResponse> findAll(String nameKeyword) {
        String keyword = Optional.ofNullable(nameKeyword).orElse("");
        return categoryRepository.findAll(keyword).stream().map(baseCategoryResponseMapper::mapping)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> groupingCategoryResponse(Collection<CategoryResponse> categoryResponses) {
        List<CategoryResponse> parents = new ArrayList<>();
        List<CategoryResponse> childs = new ArrayList<>();
        categoryResponses.parallelStream().forEach(categoryResponse -> {
            if (categoryResponse.getParentId() == null) {
                parents.add(categoryResponse);
            } else {
                childs.add(categoryResponse);
            }
        });
        childs.stream().forEach(child -> {
            Optional<CategoryResponse> categoryOptional = parents
                    .stream()
                    .filter(v -> v.getId() == child.getParentId())
                    .findFirst();
            if (!categoryOptional.isPresent()) {
                return;
            }

            CategoryResponse category = categoryOptional.get();
            if (category.getChildrens() == null) {
                category.setChildrens(new ArrayList<>());
            }
            category.getChildrens().add(child);
        });
        return parents;
    }

    @Override
    public List<CategoryResponse> fetchCategoriesHasProductSellable() {
        List<CategoryResponse> categoryResponses = categoryRepository.fetchCategoriesHasProductSellable().stream()
                .map(categoryResponseMapper::mapping).collect(Collectors.toList());
        return groupingCategoryResponse(categoryResponses);
    }

    @Override
    public Set<CategoryProjections> fetchCategoriesGroupProducts() {
        return categoryRepository.findParentOnly()
            .stream()
            .map(c -> {
                CategoryProjections category = new CategoryProjections();
                category.setId(c.getId());
                category.setName(c.getName());
                category.setProducts(productRepository.findAllByDiscountNotAvailable(c.getId()));
                return category;
            })
            .filter(c -> !c.getProducts().isEmpty())
            .collect(Collectors.toSet());
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
        categoryRepository.deleteAllCategoryParameterTypeBy(id);
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

        @Autowired
        private ProductCategoryRepository productCategoryRepository;

        @Override
        public BaseCategoryResponse mapping(Category category) {
            BaseCategoryResponse baseCategoryResponse = new BaseCategoryResponse();
            baseCategoryResponse.setId(category.getId());
            baseCategoryResponse.setCategoryName(category.getCategoryName());
            baseCategoryResponse.setProductCount(productCategoryRepository.countByCategoryId(category.getId()));
            Optional.ofNullable(category.getParent())
                    .ifPresent(parent -> baseCategoryResponse.setParentId(parent.getId()));
            return baseCategoryResponse;
        }

    }

    @Lazy
    @Component
    class CategoryResponseMapper implements Mapper<CategoryResponse, ICategoryReponse> {

        @Override
        public CategoryResponse mapping(ICategoryReponse iCategory) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(iCategory.getId());
            categoryResponse.setCategoryName(iCategory.getCategoryName());
            categoryResponse.setProductCount(iCategory.getProductCount());
            categoryResponse.setParentId(iCategory.getParentId());

            return categoryResponse;
        }

    }

}