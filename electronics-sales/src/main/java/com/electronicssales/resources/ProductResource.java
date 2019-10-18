package com.electronicssales.resources;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.electronicssales.entities.Product;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.models.responses.FetchProductOption;
import com.electronicssales.models.responses.ProductResponse;
import com.electronicssales.models.types.FetchProductType;
import com.electronicssales.models.types.ProductSortType;
import com.electronicssales.models.types.SortType;
import com.electronicssales.services.ProductService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/api/products")
public class ProductResource {

    @Lazy
    @Autowired
    private ProductService productService;

    @Lazy
    @Autowired
    private Mapper<ProductResponse, Product> productResponseMapper;

    private boolean validateProductBeforeCreate(ProductDto product) {
        if(productService.existsByProductName(product.getProductName())) {
            throw new EntityExistsException("Product is already exists !");
        }
        return true;
    }

    private boolean validateProductBeforeUpdate(ProductDto productCheckable) {
        Product productFinded = productService
            .findByProductId(productCheckable.getId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found !"));

        if(!productFinded.getProductName().equalsIgnoreCase(productCheckable.getProductName())
           && productService.existsByProductName(productCheckable.getProductName()) ) 
            throw new EntityExistsException("Product name is aleady exists !");

        return true;
    }

    @GetMapping
    public ResponseEntity<?> fetchProducts(
        @RequestParam(value = "categoriesId", required = false) 
        List<Long> categoriesId,
        @RequestParam(value = "manufacturersId", required = false)
        List<Long> manufacturersId,
        @RequestParam(value = "fromPrice", required = false, defaultValue = "0")
        long fromPrice,
        @RequestParam(value = "toPrice", required = false, defaultValue = "0")
        Long toPrice,
        @RequestParam(value = "productSortType", required = false)
        String productSortType,
        @RequestParam(value = "sortType", required = false)
        String sortType,
        @RequestParam(value = "p", required = false, defaultValue = "0")
        int page,
        @RequestParam(value = "s", required = false, defaultValue = "10")
        int size,
        @RequestParam(value = "search", required = false)
        String searchKey,
        @RequestParam(value = "fetchType", required = false)
        String fetchType
    ) {
        
        FetchProductOption option = new FetchProductOption();
        option.setCategoriesId(Optional.ofNullable(categoriesId).orElse(Collections.emptyList()));
        option.setManufacturersId(Optional.ofNullable(manufacturersId).orElse(Collections.emptyList()));
        option.setFromPrice(fromPrice);
        option.setToPrice(toPrice);
        option.setSearchKey(searchKey);
        option.setPageable(PageRequest.of(page, size));
        option.setFetchProductType(FetchProductType.of(fetchType));

        if(productSortType != null) {
            option.setProductSortType(ProductSortType.of(productSortType));
        }

        if(sortType != null) {
            option.setSortType(SortType.of(sortType));
        }
        
        return ResponseEntity
            .ok(productService.fetchProductsBy(option));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchProduct(@PathVariable long id) {
        Product productFinded = productService.findByProductId(id)
            .orElseThrow(() -> new EntityNotFoundException("Product with id not found !"));
        
        return ResponseEntity  
            .ok(productResponseMapper.mapping(productFinded));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto productDto) {
        this.validateProductBeforeCreate(productDto);

        return ResponseEntity
            .created(null)
            .body(productService.createProduct(productDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
        @RequestBody @Valid ProductDto productDto, 
        @PathVariable("id") long productId
    ) 
    {
        productDto.setId(productId);
        validateProductBeforeUpdate(productDto);

        return ResponseEntity
            .created(null)
            .body(productService.updateProduct(productDto));
    }

    @GetMapping("/{id}/parameters")
    public ResponseEntity<?> fetchProductParameters(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProductParametersByProductId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id) {
        if(!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}