package com.electronicssales.services.impls;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.Discount;
import com.electronicssales.entities.Image;
import com.electronicssales.entities.ProductCategory;
import com.electronicssales.entities.ProductParameter;
import com.electronicssales.entities.Manufacturer;
import com.electronicssales.entities.Product;
import com.electronicssales.entities.ParameterType;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.models.responses.BaseCategoryResponse;
import com.electronicssales.models.responses.CategoryResponse;
import com.electronicssales.models.responses.DiscountResponse;
import com.electronicssales.models.responses.FetchProductOption;
import com.electronicssales.models.responses.ICategoryReponse;
import com.electronicssales.models.responses.IParagraphResponse;
import com.electronicssales.models.responses.ImageDataResponse;
import com.electronicssales.models.responses.ParagraphResponse;
import com.electronicssales.models.responses.ProductDiscountResponse;
import com.electronicssales.models.responses.ProductParameterRepositoryResponse;
import com.electronicssales.models.responses.ProductParameterResponse;
import com.electronicssales.models.responses.ProductResponse;
import com.electronicssales.models.types.ProductStatus;
import com.electronicssales.repositories.CategoryRepository;
import com.electronicssales.repositories.ParagraphRepository;
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
import org.springframework.web.multipart.MultipartFile;

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

    @Lazy
    @Autowired
    private Mapper<BaseCategoryResponse, Category> baseCategoryResponseMapper;

    @Override
    @Transactional
    public Product saveProduct(ProductDto productDto) {
        return Optional.of(productDto).map(productMapper::mapping).map(productRepository::save).get();
    }

    @Transactional
    @Override
    public ProductResponse createProduct(ProductDto productDto) {
        return Optional.of(productDto).map(productMapper::mapping).map(productRepository::save)
                .map(productResponseMapper::mapping).get();
    }

    @Transactional
    @Override
    public List<ProductResponse> fetchProductsBy(FetchProductOption option) {
        return productRepository.fetchProductsBy(option).stream().map(productResponseMapper::mapping)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(ProductDto productDto) {
        return Optional.of(productDto).map(productMapper::mapping).map(productRepository::merge)
                .map(productResponseMapper::mapping).get();
    }

    private ProductParameterResponse parseFrom(ProductParameterRepositoryResponse repoResponse) {
        ProductParameterResponse productParameterResponse = new ProductParameterResponse();
        productParameterResponse.setParameterType(repoResponse.getParameterType());
        productParameterResponse.setParameterValue(repoResponse.getParameterValue());
        productParameterResponse.setId(repoResponse.getId());
        return productParameterResponse;
    }

    @Override
    public Collection<ProductParameterResponse> getProductParametersByProductId(long productId) {
        return productParameterRepository.fetchByProductId(productId).stream().map(this::parseFrom)
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

    @Override
    public List<ParagraphResponse> getDescriptionsOf(long productId) {
        return productRepository.findParagraphsByProductId(productId).stream().map(paragraphResponseMapper::mapping)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findByName(String productName) {
        return productRepository.findByProductName(productName);
    }

    @Override
    public List<ImageDataResponse> getImages(long productId) {
        return productRepository.findImageByProductId(productId).getImages().stream().map(image -> image.getData())
                .map(ImageDataResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<BaseCategoryResponse> getCategoriesBy(long productId) {
        return categoryRepository.findByProductId(productId).stream().map(baseCategoryResponseMapper::mapping)
                .collect(Collectors.toList());
    }

    @Override
    public long countProductBy(FetchProductOption option) {
        return productRepository.countBy(option);
    }

    @Lazy
    @Component
    class ProductMapper implements Mapper<Product, ProductDto> {

        @Override
        public Product mapping(ProductDto productDto) {
            Product product = new Product();
            product.setId(productDto.getId());
            product.setPrice(productDto.getPrice());
            Optional.ofNullable(productDto.getManufacturerId()).ifPresent(manufacturerId -> {
                if (manufacturerId > 0) {
                    product.setManufacturer(Manufacturer.of(manufacturerId));
                }
                ;
            });
            product.setProductName(productDto.getProductName());
            product.setStatus(ProductStatus.SELLABLE);
            product.setDescriptions(productDto.getParagraphs());

            Optional.ofNullable(productDto.getCategoryIds()).ifPresent(categoryIds -> {
                List<ProductCategory> productCategories = categoryIds.stream()
                        .map(categoryId -> ProductCategory.of(Category.of(categoryId), product))
                        .collect(Collectors.toList());
                product.setProductCategories(productCategories);
            });

            Optional.ofNullable(productDto.getImages()).ifPresent(files -> {
                List<Image> images = mapToImages(files);
                product.setImages(images);
            });

            Optional.ofNullable(productDto.getProductParameters()).ifPresent(productParameterDtos -> {
                List<ProductParameter> productParameters = productParameterDtos.stream()
                        .map(productParameterDto -> ProductParameter.of(product,
                                ParameterType.of(productParameterDto.getParameterTypeId()),
                                productParameterDto.getParameterValue()))
                        .collect(Collectors.toList());

                product.setProductParameters(productParameters);
            });
            return product;
        }

        private List<Image> mapToImages(MultipartFile[] files) {
            return Stream.of(files).map(file -> {
                try {
                    return file.getBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(data -> data != null).map(Image::of).collect(Collectors.toList());
        }

    }

    @Lazy
    @Component
    public class ProductResponseMapper implements Mapper<ProductResponse, Product> {

        @Override
        public ProductResponse mapping(Product product) {
            ProductResponse productResponse = new ProductDiscountResponse();
            productResponse.setId(product.getId());
            productResponse.setProductName(product.getProductName());
            productResponse.setQuantity(product.getQuantity());
            productResponse.setPrice(product.getPrice());
            Optional<Manufacturer> manufacturerOptional = Optional.ofNullable(product.getManufacturer());
            if (manufacturerOptional.isPresent()) {
                productResponse.setManufacturerId(manufacturerOptional.map(manufacturer -> manufacturer.getId()).get());
            }
            productResponse.setCreatedTime(product.getCreatedTime());
            productResponse.setUpdatedTime(product.getUpdatedTime());
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
            ProductDiscountResponse productDiscountResponse = (ProductDiscountResponse) productResponseMapper
                    .mapping(product);
            Optional.ofNullable(product.getDiscount()).ifPresent(
                    discount -> productDiscountResponse.setDiscount(discountResponseMapper.mapping(discount)));
            return productDiscountResponse;
        }

    }

}