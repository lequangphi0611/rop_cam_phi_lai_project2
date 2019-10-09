package com.electronicssales.resources;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.electronicssales.entities.Category;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.responses.CategoryResponse;
import com.electronicssales.services.CategoryService;
import com.electronicssales.services.ManufacturerService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDto categoryDto) {

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
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createAll(@RequestBody Collection<CategoryDto> categoryDtos) {
        return ResponseEntity
            .created(null)
            .body(categoryService.saveAll(categoryDtos));
    }

    @GetMapping
    public ResponseEntity<?> fetchCategories(
        @RequestParam(required = false, value = "q", defaultValue = "") 
        String query) 
    {
        return ResponseEntity.ok(categoryService.findAll(query));
    }

    @GetMapping("/{categoryId}/manufacturers")
    public ResponseEntity<?> fetchManufacturers(@PathVariable long categoryId) {
        return ResponseEntity.ok(categoryService.fetchManufacturersByCategoryId(categoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
        @RequestBody @Valid CategoryDto categoryDto,
        @PathVariable("id") long id
    ) 
    {
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
    }

    @GetMapping("/{id}/childrens")
    public ResponseEntity<?> fetchChildrensOf(
            @PathVariable("id") long parentId,
            @RequestParam(required = false, value = "q", defaultValue = "") 
            String query) {
        return ResponseEntity.ok(categoryService.fetchChildrensOf(parentId, query));
    } 

    @GetMapping("/{id}/parameter-types")
    public ResponseEntity<?> fetchParametersByCategoryId(@PathVariable("id") long categoryId) {
        return ResponseEntity.ok(categoryService.fetchParameterTypeByCategoryId(categoryId));
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}