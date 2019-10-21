package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.electronicssales.entities.Discount;
import com.electronicssales.entities.Product;
import com.electronicssales.models.dtos.DiscountDto;
import com.electronicssales.models.responses.DiscountFullResponse;
import com.electronicssales.models.responses.DiscountResponse;
import com.electronicssales.models.responses.ProductResponse;
import com.electronicssales.models.types.DiscountType;
import com.electronicssales.repositories.DiscountRepository;
import com.electronicssales.repositories.ProductRepository;
import com.electronicssales.services.DiscountService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class DefaultDiscountService implements DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Lazy
    @Autowired
    private Mapper<Discount, DiscountDto> discountMapper;

    @Lazy
    @Autowired
    private Mapper<DiscountResponse, Discount> discountResponseMapper;

    @Lazy
    @Autowired
    private Mapper<DiscountFullResponse, Discount> discountFullResponseMapper;

    @Lazy
    @Autowired
    private Mapper<ProductResponse, Product> productResponseMapper;

    private Stream<Product> getProductsFromProductIds(Collection<Long> productIds) {
        return productIds
            .stream()
            .map(productId -> productRepository.findByIdWithoutFetchDiscount(productId).get());
    }

    @Transactional
    @Override
    public Discount saveDiscount(DiscountDto discountDto) {
        Discount discountTransient = discountMapper.mapping(discountDto);
        List<Product> products = getProductsFromProductIds(discountDto.getProductIds())
            .peek(product -> product.setDiscount(discountTransient))
            .collect(Collectors.toList());
        discountTransient.setProducts(products);
        return discountRepository.save(discountTransient);
    }

    @Transactional
    @Override
    public List<DiscountFullResponse> fetchDiscounts() {
        return discountRepository
            .findAll()
            .stream()
            .map(discountFullResponseMapper::mapping)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Discount updateDiscount(DiscountDto discountDto) {
        Discount discount = discountMapper.mapping(discountDto);
        productRepository.removeAllDiscount(discount.getId());
        List<Product> products = getProductsFromProductIds(discountDto.getProductIds())
            .peek(product -> product.setDiscount(discount))
            .collect(Collectors.toList());
        discount.setProducts(products);
        return discountRepository.save(discount);
    }

    @Transactional
    @Override
    public void deleteById(long discountId) {
        discountRepository.deleteById(discountId);
    }

    @Override
    public List<ProductResponse> getProducts(long discountId) {
        return productRepository.findByDiscountId(discountId)
            .stream()
            .map(productResponseMapper::mapping)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Discount> findById(long id) {
        return discountRepository
            .findById(id)
            .filter(discount -> discount != null);
    }

    @Lazy
    @Component
    public class DiscountMapper implements Mapper<Discount, DiscountDto> {

        @Override
        public Discount mapping(DiscountDto discountDto) {
            Discount discount = new Discount();
            discount.setId(discountDto.getId());
            DiscountType discountType = DiscountType.of(discountDto.getDiscountType())
                .orElse(DiscountType.PERCENT);
            discount.setDiscountType(discountType);
            discount.setStartedTime(discountDto.getStatedTime());
            discount.setDiscountValue(discountDto.getDiscountValue());
            return discount;
        }
    
        
    }

    @Lazy
    @Component
    public class DiscountResponseMapper implements Mapper<DiscountResponse, Discount> {

        @Override
        public DiscountResponse mapping(Discount discount) {
            DiscountResponse discountResponse = new DiscountFullResponse();
            discountResponse.setId(discount.getId());
            discountResponse.setStartedTime(discount.getStartedTime());
            discountResponse.setDiscountType(discount.getDiscountType().toString());
            discountResponse.setDiscountValue(discount.getDiscountValue());
            return discountResponse;
        }
        
    }

    @Lazy
    @Component
    public class DiscountFullResponseMapper implements Mapper<DiscountFullResponse, Discount> {

        @Lazy
        @Autowired
        private Mapper<DiscountResponse, Discount> discountResponseMapper;

        @Autowired
        private ProductRepository productRepository;

        public DiscountFullResponseMapper() {
            System.out.println("CReated Mapper reponse !");
        }

        @Override
        public DiscountFullResponse mapping(Discount discount) {
            DiscountFullResponse discountFullResponse = 
                (DiscountFullResponse) discountResponseMapper.mapping(discount);
            discountFullResponse.setProductCount(productRepository.countByDiscountId(discount.getId()));
            return discountFullResponse;
        }
    
        
    }
    
}