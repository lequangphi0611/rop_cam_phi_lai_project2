package com.electronicssales.resources;

import java.util.concurrent.Callable;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.electronicssales.entities.Category;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.responses.CategoryResponse;
import com.electronicssales.models.types.CategoryFetchType;
import com.electronicssales.services.CategoryService;
import com.electronicssales.services.ManufacturerService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    @Lazy
    @Autowired
    private CategoryService categoryService;

    @Lazy
    @Autowired
    private ManufacturerService manufacturerService;

    @Lazy
    @Autowired
    private Mapper<CategoryResponse, Category> categoryResponseMapper;

    @PostMapping
    public Callable<ResponseEntity<?>> createCategory(@RequestBody @Valid CategoryDto categoryDto) {

        return () -> {
            if(categoryService.existsByCategoryName(categoryDto.getCategoryName())) {
                throw new EntityExistsException(
                    new StringBuilder(Category.class.getSimpleName())
                        .append(" with categoryName is already exists !")
                        .toString()
                );
            }
    
            return ResponseEntity
                .created(null)
                .body(categoryService.createCategory(categoryDto));
        };
    }

    @GetMapping
    public Callable<ResponseEntity<?>> fetchCategories(
        @RequestParam(required = false, value = "q", defaultValue = "") 
        String query,
        @RequestParam(required = false, defaultValue = "PARENT_ONLY")
        String fetchType,
        @RequestParam(required = false, defaultValue = "0")
        int page,
        @RequestParam(required = false)
        Integer size,
        @RequestParam(required = false, defaultValue = "id")
        String sortBy,
        @RequestParam(required = false, defaultValue = "desc")
        String sortType) 
    {
        return () -> {
            CategoryFetchType fetch = CategoryFetchType.of(fetchType).get();
            if(fetch == CategoryFetchType.PARENT_ONLY) {
                return ResponseEntity.ok(categoryService.findAllAndFetchChildrens(query));
            }
            if(size == null || size < 0) {
                return ResponseEntity.ok(categoryService.findAll(query));
            }
            Direction direction = Direction.valueOf(sortType.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, new Sort(direction, sortBy));
            return ResponseEntity.ok(categoryService.findAll(pageable, query));
        };
    }

    @GetMapping("/{categoryId}/manufacturers")
    public Callable<ResponseEntity<?>> fetchManufacturers(@PathVariable long categoryId) {
        return () -> ResponseEntity.ok(manufacturerService.findByCategoryId(categoryId));
    }

    @PutMapping("/{id}")
    public Callable<ResponseEntity<?>> updateCategory(
        @RequestBody @Valid CategoryDto categoryDto,
        @PathVariable("id") long id
    ) 
    {
        return () -> {
            Category categoryFinded = categoryService
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category with id not found !"));

        if(!categoryFinded.getCategoryName().equals(categoryDto.getCategoryName())
            && categoryService.existsByCategoryName(categoryDto.getCategoryName())) {
                throw new EntityExistsException("Category with name is already exists !");
        }

        categoryDto.setId(id);
        return ResponseEntity
            .created(null)
            .body(categoryService.updateCategory(categoryDto));
        };
    }

    @GetMapping("/{id}/childrens")
    public Callable<ResponseEntity<?>> fetchChildrensOf(
            @PathVariable("id") long parentId,
            @RequestParam(required = false, value = "q", defaultValue = "") 
            String query) {
        return () -> ResponseEntity.ok(categoryService.fetchChildrensOf(parentId, query));
    } 

    @GetMapping("/{id}/parameter-types")
    public Callable<ResponseEntity<?>> fetchParametersByCategoryId(@PathVariable("id") long categoryId) {
        return () -> ResponseEntity.ok(categoryService.fetchParameterTypeByCategoryId(categoryId));
    } 

    @DeleteMapping("/{id}")
    public Callable<ResponseEntity<?>> deleteCategory(@PathVariable long id) {
        return () -> {
            categoryService.deleteCategoryById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        };
    }
    
}