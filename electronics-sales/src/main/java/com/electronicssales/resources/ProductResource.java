package com.electronicssales.resources;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.electronicssales.entities.Product;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.models.responses.FetchProductOption;
import com.electronicssales.models.responses.ProductDiscountResponse;
import com.electronicssales.models.responses.ProductResponse;
import com.electronicssales.models.types.FetchProductType;
import com.electronicssales.models.types.ProductSortType;
import com.electronicssales.models.types.SortType;
import com.electronicssales.services.ProductService;
import com.electronicssales.utils.Mapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.Value;

@RestController
@RequestMapping("/api/products")
public class ProductResource {

    @Lazy
    @Autowired
    private ProductService productService;

    @Lazy
    @Autowired
    private Mapper<ProductResponse, Product> productResponseMapper;

    @Lazy
    @Autowired
    private Mapper<ProductDiscountResponse, Product> productDiscountResponseMapper;

    @Lazy
    @Autowired
    private ObjectMapper objectMapper;

    private boolean validateProductBeforeCreate(ProductDto product) {
        if (productService.existsByProductName(product.getProductName())) {
            throw new EntityExistsException("Product is already exists !");
        }
        return true;
    }

    private boolean validateProductBeforeUpdate(ProductDto productCheckable) {
        Product productFinded = productService.findByProductId(productCheckable.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found !"));

        if (!productFinded.getProductName().equalsIgnoreCase(productCheckable.getProductName())
                && productService.existsByProductName(productCheckable.getProductName()))
            throw new EntityExistsException("Product name is aleady exists !");
        productCheckable.setQuantity(productFinded.getQuantity());
        return true;
    }

    @GetMapping
    public Callable<ResponseEntity<?>> fetchProducts(
            @RequestParam(value = "categoriesId", required = false) List<Long> categoriesId,
            @RequestParam(value = "manufacturersId", required = false) List<Long> manufacturersId,
            @RequestParam(value = "fromPrice", required = false, defaultValue = "0") long fromPrice,
            @RequestParam(value = "toPrice", required = false, defaultValue = "0") Long toPrice,
            @RequestParam(value = "productSortType", required = false) String productSortType,
            @RequestParam(value = "sortType", required = false) String sortType,
            @RequestParam(value = "p", required = false, defaultValue = "0") int page,
            @RequestParam(value = "s", required = false, defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String searchKey,
            @RequestParam(value = "fetchType", required = false) String fetchType,
            @RequestParam(defaultValue = "true") boolean fetchDiscount) {
        return () -> {

            FetchProductOption option = new FetchProductOption();
            option.setCategoriesId(Optional.ofNullable(categoriesId).orElse(Collections.emptyList()));
            option.setManufacturersId(Optional.ofNullable(manufacturersId).orElse(Collections.emptyList()));
            option.setFromPrice(fromPrice);
            option.setToPrice(toPrice);
            option.setSearchKey(searchKey);
            option.setPageable(PageRequest.of(page, size));
            option.setFetchProductType(FetchProductType.of(fetchType));

            if (StringUtils.hasText(productSortType)) {
                option.setProductSortType(ProductSortType.of(productSortType));
            }

            option.setSortType(SortType.of(sortType));
            option.setFetchDiscount(fetchDiscount);

            return ResponseEntity.ok(productService.fetchProductsBy(option));
        };
    }

    @GetMapping("/count")
    public Callable<ResponseEntity<?>> countProduct(
            @RequestParam(value = "categoriesId", required = false) List<Long> categoriesId,
            @RequestParam(value = "manufacturersId", required = false) List<Long> manufacturersId,
            @RequestParam(value = "fromPrice", required = false, defaultValue = "0") long fromPrice,
            @RequestParam(value = "toPrice", required = false, defaultValue = "0") Long toPrice,
            @RequestParam(value = "productSortType", required = false) String productSortType,
            @RequestParam(value = "sortType", required = false) String sortType,
            @RequestParam(value = "p", required = false, defaultValue = "0") int page,
            @RequestParam(value = "s", required = false, defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String searchKey,
            @RequestParam(value = "fetchType", required = false) String fetchType,
            @RequestParam(defaultValue = "true") boolean fetchDiscount) {
        return () -> {
            FetchProductOption option = new FetchProductOption();
            option.setCategoriesId(Optional.ofNullable(categoriesId).orElse(Collections.emptyList()));
            option.setManufacturersId(Optional.ofNullable(manufacturersId).orElse(Collections.emptyList()));
            option.setFromPrice(fromPrice);
            option.setToPrice(toPrice);
            option.setSearchKey(searchKey);
            option.setPageable(PageRequest.of(page, size));
            option.setFetchProductType(FetchProductType.of(fetchType));

            if (StringUtils.hasText(productSortType)) {
                option.setProductSortType(ProductSortType.of(productSortType));
            }

            option.setSortType(SortType.of(sortType));
            option.setFetchDiscount(fetchDiscount);
            return ResponseEntity.ok(Optional.of(productService.countProductBy(option))
                .map(CountProductResponse::new).get());
        };
    }

    @GetMapping("/{id}")
    public Callable<ResponseEntity<?>> fetchProduct(@PathVariable long id) {
        return () -> {
            Product productFinded = productService.findByProductId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Product with id not found !"));

            return ResponseEntity.ok(productDiscountResponseMapper.mapping(productFinded));
        };
    }

    @RequestMapping(path = "/product-name/{productName}", method = RequestMethod.HEAD)
    public ResponseEntity<?> existsByProductByName(@PathVariable String productName) {
        Optional<Product> productFinded = productService.findByName(productName);
        if (!productFinded.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<?> createProduct(@RequestParam("product") String productDtoString,
            @RequestParam(required = false) MultipartFile[] images)
            throws JsonParseException, JsonMappingException, IOException {

        ProductDto productDto = this.objectMapper.readValue(productDtoString, ProductDto.class);
        this.validateProductBeforeCreate(productDto);
        productDto.setImages(images);
        Optional.ofNullable(images).ifPresent(productDto::setImages);
        ProductResponse productCreated = productService.createProduct(productDto);
        return ResponseEntity.created(null).body(productCreated);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateProduct(@PathVariable("id") long productId, @RequestParam("product") String productStr,
            @RequestParam(required = false) MultipartFile[] images)
            throws JsonParseException, JsonMappingException, IOException {
        ProductDto product = this.objectMapper.readValue(productStr, ProductDto.class);
        product.setId(productId);
        validateProductBeforeUpdate(product);
        Optional.ofNullable(images).ifPresent(product::setImages);
        ProductResponse productUpdated = productService.updateProduct(product);
        return ResponseEntity.created(null).body(productUpdated);
    }

    @GetMapping("/{id}/parameters")
    public Callable<ResponseEntity<?>> fetchProductParameters(@PathVariable long id) {
        return () -> ResponseEntity.ok(productService.getProductParametersByProductId(id));
    }

    @GetMapping(value = "/{id}/images")
    public Callable<ResponseEntity<?>> fetchProductImages(@PathVariable long id) {
        return () -> ResponseEntity.ok(productService.getImages(id));
    }

    @DeleteMapping("/{id}")
    public Callable<ResponseEntity<?>> deleteProduct(@PathVariable long id) {
        return () -> {
            if (!productService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            productService.deleteById(id);
            return ResponseEntity.ok().build();
        };
    }

    @GetMapping("/{id}/descriptions")
    public Callable<ResponseEntity<?>> fetchDescriptions(@PathVariable("id") long productId) {
        return () -> ResponseEntity.ok(productService.getDescriptionsOf(productId));
    }

    @GetMapping("/{id}/categories")
    public Callable<ResponseEntity<?>> fetchCategories(@PathVariable("id") long productId) {
        return () -> ResponseEntity.ok(productService.getCategoriesBy(productId));
    }

    @RequestMapping(value = "/productName/{productName}", method = RequestMethod.HEAD)
    public Callable<ResponseEntity<?>> existsByProductName(@PathVariable String productName) {
        return () -> productService.existsByProductName(productName)
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
    }

    @Value
    public class CountProductResponse {
        
        private long count;
    }

}