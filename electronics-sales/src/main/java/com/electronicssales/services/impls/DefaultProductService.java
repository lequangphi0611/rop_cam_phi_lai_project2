package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.stream.Collectors;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.Image;
import com.electronicssales.entities.Manufacturer;
import com.electronicssales.entities.Paragraph;
import com.electronicssales.entities.ParameterType;
import com.electronicssales.entities.Product;
import com.electronicssales.entities.ProductDescription;
import com.electronicssales.entities.ProductImage;
import com.electronicssales.entities.ProductParameter;
import com.electronicssales.models.dtos.ParagraphDto;
import com.electronicssales.models.dtos.ProductDto;
import com.electronicssales.models.dtos.ProductParameterDto;
import com.electronicssales.models.responses.ProductParameterResponse;
import com.electronicssales.models.responses.ProductParameterRepositoryResponse;
import com.electronicssales.repositories.ParagraphRepository;
import com.electronicssales.repositories.ProductDescriptionRepository;
import com.electronicssales.repositories.ProductImageRepository;
import com.electronicssales.repositories.ProductParameterRepository;
import com.electronicssales.repositories.ProductRepository;
import com.electronicssales.services.ProductService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultProductService implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ParagraphRepository paragraphRepository;

    @Autowired
    private ProductDescriptionRepository productDescriptionRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductParameterRepository productParameterRepository;

    @Autowired
    private Mapper<Product, ProductDto> productMapper;

    @Autowired
    private Mapper<ProductParameter, ProductParameterDto> productParameterMapper;

    private Paragraph parseParagraph(ParagraphDto paragraphDto) {
        Paragraph paragraph = new Paragraph();
        paragraph.setId(paragraphDto.getId());
        if(paragraphDto.getImageId() > 0) {
            paragraph.setImage(new Image(paragraphDto.getImageId()));
        }
        paragraph.setTitle(paragraphDto.getTitle());
        paragraph.setText(paragraphDto.getText());
        return paragraph;
    }

    private ProductDescription getProductDescriptionFrom(Paragraph paragraph, long productId){
        ProductDescription productDescription = new ProductDescription();
        productDescription.setParagraph(paragraph);
        productDescription.setProduct(new Product(productId));
        return productDescription;
    };

    private ProductImage getProductImageFrom(long productId, Image image) {
        ProductImage productImage = new ProductImage();
        productImage.setImage(image);
        productImage.setProduct(new Product(productId));
        return productImage; 
    }

    private Collection<ProductDescription> saveProductDescriptionsFrom(long productId, Collection<ParagraphDto> paragraphDtos) {
        return paragraphDtos
            .stream()
            .map(this::parseParagraph)
            .map(paragraphRepository::save)
            .map(paragraph -> this.getProductDescriptionFrom(paragraph, productId))
            .map(productDescriptionRepository::save)
            .collect(Collectors.toList());
    }

    private Collection<ProductImage> saveProductImagesFrom(long productId, Collection<Image> images) {
        return images
            .stream()
            .map(image -> getProductImageFrom(productId, image))
            .map(productImageRepository::save)
            .collect(Collectors.toList());
    }

    private Collection<ProductParameter> saveProductParametersFrom(long productId, Collection<ProductParameterDto> productParameterDtos) {
        return productParameterDtos
            .stream()
            .peek((parameterDto) -> parameterDto.setProductId(productId))
            .map(productParameterMapper::mapping)
            .map(productParameterRepository::save)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Product saveProduct(ProductDto productDto) {
        Product productSaved = productRepository.save(productMapper.mapping(productDto));

        final long productSavedId = productSaved.getId();

        saveProductDescriptionsFrom(productSavedId, productDto.getParagraphDtos());
        
        Collection<Image> images = productDto.getImageIds()
            .stream()
            .map(imageId -> new Image(imageId))
            .collect(Collectors.toList());
        saveProductImagesFrom(productSavedId, images);
        
        saveProductParametersFrom(productSavedId, productDto.getProductParameterDtos());

        return productSaved;
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

    @Component
    class ProductMapper implements Mapper<Product, ProductDto> {

        @Override
        public Product mapping(ProductDto productDto) {
            Product product = new Product();
            product.setId(productDto.getId());
            product.setPrice(productDto.getPrice());
            product.setManufacturer(new Manufacturer(productDto.getManufacturerId()));
            product.setCategory(new Category(productDto.getCategoryId()));
            product.setProductName(productDto.getProductName());
            product.setSalable(true);
            return product;
        }

    }

    @Component
    class ProductParameterMapper implements Mapper<ProductParameter, ProductParameterDto> {

        @Override
        public ProductParameter mapping(ProductParameterDto productParameterDto) {
            ProductParameter productParameter = new ProductParameter();
            productParameter.setProduct(new Product(productParameterDto.getProductId()));
            productParameter.setId(productParameterDto.getId());
            productParameter.setParameterType(new ParameterType(productParameterDto.getParameterTypeId()));
            productParameter.setParameterValue(productParameterDto.getParameterValue());
            return productParameter;
        }

    } 
    
}