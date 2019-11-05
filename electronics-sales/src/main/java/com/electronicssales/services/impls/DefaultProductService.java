package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.Discount;
import com.electronicssales.entities.Manufacturer;
import com.electronicssales.entities.ParameterType;
import com.electronicssales.entities.Product;
import com.electronicssales.entities.ProductParameter;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.models.dtos.ProductParameterDto;
import com.electronicssales.models.responses.DiscountResponse;
import com.electronicssales.models.responses.FetchProductOption;
import com.electronicssales.models.responses.IParagraphResponse;
import com.electronicssales.models.responses.ImageDataResponse;
import com.electronicssales.models.responses.ParagraphResponse;
import com.electronicssales.models.responses.ProductDiscountResponse;
import com.electronicssales.models.responses.ProductParameterRepositoryResponse;
import com.electronicssales.models.responses.ProductParameterResponse;
import com.electronicssales.models.responses.ProductResponse;
import com.electronicssales.repositories.CategoryRepository;
import com.electronicssales.repositories.ParagraphRepository;
import com.electronicssales.repositories.ProductCategoryRepository;
import com.electronicssales.repositories.ProductParameterRepository;
import com.electronicssales.repositories.ProductRepository;
import com.electronicssales.services.ProductService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class DefaultProductService implements ProductService {

    @Lazy
    @Autowired
    private ProductRepository productRepository;
    
    @Lazy
    @Autowired
    private ParagraphRepository paragraphRepository;

    @Lazy
    @Autowired
    private ProductParameterRepository productParameterRepository;

    @Lazy
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Lazy
    @Autowired
    private CategoryRepository categoryRepository;

    @Lazy
    @Autowired
    private Mapper<Product, ProductDto> productMapper;

    @Lazy
    @Autowired
    private Mapper<ProductResponse, Product> productResponseMapper;

    @Lazy
    @Autowired
    private Mapper<ProductDiscountResponse, Product> productDiscountResponseMapper;

    @Lazy
    @Autowired
    private Mapper<ParagraphResponse, IParagraphResponse> paragraphResponseMapper;

    @Override
    @Transactional
    public Product saveProduct(ProductDto productDto) {
        Product productSaved = productRepository.save(productMapper.mapping(productDto));

        return productSaved;
    }

    private void proccessAfterSaved(Product productPersisted, ProductDto productDto) {
        productCategoryRepository.createAll(
            productPersisted, 
            getCategoryIds(productDto.getCategoriesId())
        );

        productParameterRepository.createAll(
            productPersisted, 
            getProductParametersFrom(productDto.getProductParameters())
        );
    }

    @Transactional
    @Override
    public Product createProduct(ProductDto productDto) {
        Product productTransient = productMapper.mapping(productDto);
        Product productPersisted = productRepository.persist(productTransient);

        proccessAfterSaved(productPersisted, productDto);

        return productPersisted;
    }

    @Transactional
    @Override
    public Page<Product> fetchProductsBy(FetchProductOption option) {
        return productRepository.fetchProductsBy(option);
    }

    private void proccessAfterUpdated(Product productPersisted, ProductDto productDto) {
        productCategoryRepository.updateAll(
            productPersisted, 
            getCategoryIds(productDto.getCategoriesId())
        );

        productParameterRepository.updateAll(
            productPersisted, 
            getProductParametersFrom(productDto.getProductParameters())
        );
    }

    @Transactional
    @Override
    public Product updateProduct(ProductDto productDto) {
        Product productMapped = productMapper.mapping(productDto);

        proccessAfterUpdated(productMapped, productDto);
        
        return productRepository.merge(productMapped);
    }

    private ProductParameterResponse parseFrom(ProductParameterRepositoryResponse repoResponse) {
        ProductParameterResponse productParameterResponse = new ProductParameterResponse();
        productParameterResponse.setParameterType(repoResponse.getParameterType());
        productParameterResponse.setParameterValue(repoResponse.getParameterValue());
        productParameterResponse.setParameterId(repoResponse.getParameterId());
        return productParameterResponse;
    }
 

    @Override
    public Collection<ProductParameterResponse> getProductParametersByProductId(long productId) {
        return productParameterRepository.fetchByProductId(productId)
            .stream()
            .map(this::parseFrom)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(long productId) {
        return productRepository.existsById(productId);
    }

    @Override
    public boolean existsByProductName(String productName) {
        return productRepository.existsByProductName(productName);
    }

    @Override
    public Optional<Product> findByProductId(long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteById(long id) {
        productRepository.deleteById(id);
    }

    private Collection<ProductParameter> getProductParametersFrom(Collection<ProductParameterDto> productParameterDtos) {
        return productParameterDtos
            .stream()
            .map(this::getProductParameterFrom)
            .collect(Collectors.toList());
    }

    private ProductParameter getProductParameterFrom(ProductParameterDto productParameterDtos) {
        ProductParameter productParameter = new ProductParameter();
        productParameter.setParameterType(new ParameterType(productParameterDtos.getParameterTypeId()));
        productParameter.setParameterValue(productParameterDtos.getParameterValue());
        return productParameter;
    }

    private Collection<Category> getCategoryIds(Collection<Long> categoryIds) {
        return categoryIds
            .stream()
            .map(Category::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<ParagraphResponse> getDescriptionsOf(long productId) {
        return productRepository
            .findParagraphsByProductId(productId)
            .stream()
            .map(paragraphResponseMapper::mapping)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findByName(String productName) {
        return productRepository.findByProductName(productName);
    }

    @Override
    public List<ImageDataResponse> getImages(long productId) {
        // return productRepository.findImageByProductId(productId);
        return null;
    }

    @Lazy
    @Component
    class ProductMapper implements Mapper<Product, ProductDto> {

        @Override
        public Product mapping(ProductDto productDto) {
            Product product = new Product();
            product.setId(productDto.getId());
            product.setPrice(productDto.getPrice());
            product.setManufacturer(new Manufacturer(productDto.getManufacturerId()));
            product.setProductName(productDto.getProductName());
            product.setSalable(true);
            product.setDescriptions(productDto.getParagraphs());
            return product;
        }

    }

    @Lazy
    @Component
    public class ProductResponseMapper implements Mapper<ProductResponse, Product> {

        @Override
        public ProductResponse mapping(Product product) {
            ProductResponse productResponse = (ProductResponse) new ProductDiscountResponse();
            productResponse.setId(product.getId());
            productResponse.setProductName(product.getProductName());
            productResponse.setQuantity(product.getQuantity());
            productResponse.setPrice(product.getPrice());
            productResponse.setManufacturerId(product.getManufacturer().getId());
            return productResponse;
        }
        
    }

    @Lazy
    @Component
    public class ProductDiscountResponseMapper implements Mapper<ProductDiscountResponse, Product> {

        @Lazy
        @Autowired
        private Mapper<ProductResponse, Product> productResponseMapper;

        @Lazy
        @Autowired
        private Mapper<DiscountResponse, Discount> discountResponseMapper;

        @Override
        public ProductDiscountResponse mapping(Product product) {
            ProductDiscountResponse productDiscountResponse = 
               (ProductDiscountResponse) productResponseMapper.mapping(product);
            Optional.ofNullable(product.getDiscount())
                .ifPresent(discount -> 
                    productDiscountResponse.setDiscount(discountResponseMapper.mapping(discount))
                );
            return productDiscountResponse;
        }
    
    
    }
    
}