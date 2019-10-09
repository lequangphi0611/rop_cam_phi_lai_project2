package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.Image;
import com.electronicssales.entities.Manufacturer;
import com.electronicssales.entities.Product;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.models.responses.ProductParameterRepositoryResponse;
import com.electronicssales.models.responses.ProductParameterResponse;
import com.electronicssales.repositories.CategoryRepository;
import com.electronicssales.repositories.ParagraphRepository;
import com.electronicssales.repositories.ProductCategoryRepository;
import com.electronicssales.repositories.ProductImageRepository;
import com.electronicssales.repositories.ProductParameterRepository;
import com.electronicssales.repositories.ProductRepository;
import com.electronicssales.services.ProductService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private ProductImageRepository productImageRepository;

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

    @Override
    @Transactional
    public Product saveProduct(ProductDto productDto) {
        Product productSaved = productRepository.save(productMapper.mapping(productDto));

        return productSaved;
    }

    @Transactional
    @Override
    public Product createProduct(ProductDto productDto) {
        Product productTransient = productMapper.mapping(productDto);
        Product productPersisted = productRepository.persist(productTransient);

        productCategoryRepository.createAll(
            productPersisted, 
            productDto.getCategoriesId()
                .stream()
                .map(Category::new)
                .collect(Collectors.toList())
        );

        productParameterRepository.createAll(productPersisted, productDto.getProductParameters());

        productImageRepository.createAll(
            productPersisted, 
            productDto.getImageIds()
                .stream()
                .map(Image::new)
                .collect(Collectors.toList())
        );

        return productPersisted;
    }

    @Transactional
    @Override
    public Product updateProduct(ProductDto productDto) {
        // TODO Auto-generated method stub
        return null;
    }

    private ProductParameterResponse parseFrom(ProductParameterRepositoryResponse repoResponse) {
        ProductParameterResponse productParameterResponse = new ProductParameterResponse();
        productParameterResponse.setParameterType(repoResponse.getParameterType());
        productParameterResponse.setParameterValue(repoResponse.getParameterValue());
        productParameterResponse.setProductParameterId(repoResponse.getProductParameterId());
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
    
}