package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.electronicssales.entities.Discount;
import com.electronicssales.entities.Product;
import com.electronicssales.models.dtos.DiscountDto;
import com.electronicssales.models.responses.DiscountResponse;
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

    private Stream<Product> getProductsFromProductIds(Collection<Long> productIds) {
        return productIds
            .stream()
            .map(productId -> productRepository.findById(productId).get());
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
    public List<DiscountResponse> fetchDiscountsEnabled() {
        // TODO Auto-generated method stub
        return null;
    }

    @Transactional
    @Override
    public Discount updateDiscount(DiscountDto discountDto) {
        Discount discount = discountMapper.mapping(discountDto);
        productRepository.removeAllDiscount(discount.getId());
        List<Product> products = getProductsFromProductIds(discountDto.getProductIds()).collect(Collectors.toList());
        for(Product product : products) {
            Discount discountLocal = new Discount();
            discountLocal.setId(discount.getId());
            product.setDiscount(discountLocal);
        }
        discount.setProducts(products);
        return discountRepository.save(discount);
    }

    @Transactional
    @Override
    public void deleteById(long discountId) {
        discountRepository.deleteById(discountId);
    }

    @Lazy
    @Component
    static class DiscountMapper implements Mapper<Discount, DiscountDto> {

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
    static class DiscountResponseMapper implements Mapper<DiscountResponse, Discount> {

        @Override
        public DiscountResponse mapping(Discount discount) {
            DiscountResponse discountResponse = new DiscountResponse();
            discountResponse.setId(discount.getId());
            discountResponse.setStartedTime(discount.getStartedTime());
            discountResponse.setDiscountType(discount.getDiscountType().toString());
            discountResponse.setDiscountValue(discount.getDiscountValue());
            return discountResponse;
        }
        
    }
    
}