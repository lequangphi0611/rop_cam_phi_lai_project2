package com.electronicssales.resources;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.electronicssales.entities.Category;
import com.electronicssales.models.dtos.CategoryDto;
import com.electronicssales.models.responses.CategoryResponse;
import com.electronicssales.services.CategoryService;
import com.electronicssales.services.ManufacturerService;
import com.electronicssales.utils.AppUtil;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ManufacturerService manufacturerService;

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
            .body(categoryService.saveCategory(categoryDto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createAll(@RequestBody Collection<CategoryDto> categoryDtos) {
        Collection<CategoryDto> categories = categoryDtos
            .stream()
            .filter(categoryDto -> !categoryService.existsByCategoryName(categoryDto.getCategoryName()))
            .collect(Collectors.toSet());
        return ResponseEntity
            .created(null)
            .body(categoryService.saveAll(categories));
    }

    @GetMapping
    public ResponseEntity<?> fetchCategories(
        @RequestParam(value = "p", required = false) Integer page,
        @RequestParam(value = "l", required = false) Integer limit,
        @RequestParam(value = "q", defaultValue = "") String query
    ) 
    {
        if(limit == null) {
            return ResponseEntity.ok(categoryService.findByCategoryNameContaining(query)); 
        }
        
        if(page == null) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, limit);
        List<CategoryResponse> categoryResponses = AppUtil.parseListFrom(
                categoryService.findByCategoryNameContainingAndPageable(query, pageable), 
                categoryResponseMapper
        );
        return ResponseEntity.ok(categoryResponses);
    }

    @GetMapping("/{categoryId}/manufacturers")
    public ResponseEntity<?> fetchManufacturers(@PathVariable long categoryId) {
        return ResponseEntity.ok(manufacturerService.findByCategoryId(categoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
        @RequestBody @Valid CategoryDto categoryDto,
        @PathVariable("id") long id
    ) 
    {
        if(!categoryService.existsById(id)) {
            throw new EntityNotFoundException(
                new StringBuilder(Category.class.getSimpleName())
                .append(" with id = '")
                .append(id)
                .append("' not found !")
                .toString()
            );
        }

        categoryDto.setId(id);
        return ResponseEntity
            .created(null)
            .body(categoryService.saveCategory(categoryDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}