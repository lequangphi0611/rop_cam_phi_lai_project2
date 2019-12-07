package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.electronicssales.entities.Discount;
import com.electronicssales.entities.Product;
import com.electronicssales.models.DiscountFetchOption;
import com.electronicssales.models.DiscountProjections;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return productIds.stream().map(productId -> productRepository.findByIdWithoutFetchDiscount(productId).get());
    }

    @Transactional
    @Override
    public Discount saveDiscount(DiscountDto discountDto) {
        return this.discountRepository.create(discountDto);
    }

    @Transactional
    @Override
    public List<DiscountFullResponse> fetchDiscounts() {
        return discountRepository.findAll().stream().map(discountFullResponseMapper::mapping)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Discount updateDiscount(DiscountDto discountDto) {
        return discountRepository.update(discountDto);
    }

    @Transactional
    @Override
    public void deleteById(long discountId) {
        discountRepository.customizeDeleteById(discountId);
    }

    @Override
    public List<ProductResponse> getProducts(long discountId) {
        return productRepository.findByDiscountId(discountId).stream().map(productResponseMapper::mapping)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DiscountProjections> fetchAll(DiscountFetchOption option, Pageable pageable) {
        return discountRepository.fetchDiscounts(option, pageable);
    }

    @Override
    public Optional<Discount> findById(long id) {
        return discountRepository.findById(id).filter(discount -> discount != null);
    }

    @Lazy
    @Component
    public class DiscountMapper implements Mapper<Discount, DiscountDto> {

        @Override
        public Discount mapping(DiscountDto discountDto) {
            Discount discount = new Discount();
            discount.setId(discountDto.getId());
            DiscountType discountType = DiscountType.of(discountDto.getDiscountType()).orElse(DiscountType.PERCENT);
            // Optiona
            discount.setDiscountType(discountType);
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

        @Override
        public DiscountFullResponse mapping(Discount discount) {
            DiscountFullResponse discountFullResponse = (DiscountFullResponse) discountResponseMapper.mapping(discount);
            discountFullResponse.setProductCount(productRepository.countByDiscountId(discount.getId()));
            return discountFullResponse;
        }

    }

}