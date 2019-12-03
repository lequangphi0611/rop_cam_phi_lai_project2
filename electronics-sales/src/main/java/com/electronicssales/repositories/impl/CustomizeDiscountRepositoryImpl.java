package com.electronicssales.repositories.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.electronicssales.entities.Discount;
import com.electronicssales.models.DiscountFetchOption;
import com.electronicssales.models.DiscountProjections;
import com.electronicssales.models.dtos.DiscountDto;
import com.electronicssales.repositories.CustomizeDiscountRepository;
import com.electronicssales.repositories.ProductRepository;
import com.electronicssales.utils.GenerateSqlUtil;
import com.electronicssales.utils.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public class CustomizeDiscountRepositoryImpl implements CustomizeDiscountRepository {

    private static final Logger LOG = LoggerFactory.getLogger(CustomizeDiscountRepositoryImpl.class);

    private static final String BASE_FETCH_ALL_DISCOUNT = "SELECT d.id AS id," + " d.discount_type AS discountType,"
            + " d.discount_value as discountValue," + " d.started_time as startedTime," + " COUNT(p.id) as productCount"
            + " FROM discounts d LEFT JOIN products p ON d.id = p.discount_id";

    private static final String BASE_COUNT_DISCOUNT_QUERY = "SELECT COUNT(d.id) FROM discounts d";

    private static final String DISCOUNT_PROJECTIONS_MAPPING = "DiscountProjectionsMapping";

    private static final String[] COLUMN_GROUP = { "d.id", "d.discount_type", "d.discount_value", "d.started_time" };

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Mapper<Discount, DiscountDto> discountMapper;

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public Page<DiscountProjections> fetchDiscounts(DiscountFetchOption option, Pageable pageable) {
        Objects.requireNonNull(pageable);
        Optional<DiscountFetchOption> optionOptional = Optional.ofNullable(option);
        StringBuilder fetchAllSqlBuilder = new StringBuilder(BASE_FETCH_ALL_DISCOUNT);
        StringBuilder countSqlBuilder = new StringBuilder(BASE_COUNT_DISCOUNT_QUERY);

        // init params
        Map<String, Object> params = new HashMap<>();

        // build conditions
        optionOptional.ifPresent(v -> {
            List<StringBuilder> conditions = new ArrayList<>();
            final String statedDateField = "CAST(d.started_time as DATE)";
            if (Objects.nonNull(v.getFromDate())) {
                final String fromDateParmsStr = "fromDate";
                StringBuilder fromDateCondition = new StringBuilder(statedDateField).append(" >= :")
                        .append(fromDateParmsStr);
                conditions.add(fromDateCondition);
                params.put(fromDateParmsStr, v.getFromDate());
            }

            if (Objects.nonNull(v.getToDate())) {
                final String toDateParmsStr = "toDate";
                StringBuilder toDateCondition = new StringBuilder(statedDateField).append(" <= :")
                        .append(toDateParmsStr);
                conditions.add(toDateCondition);
                params.put(toDateParmsStr, v.getToDate());
            }

            if (!conditions.isEmpty()) {
                String conditionsJoined = conditions.stream().map(condition -> condition.toString())
                        .collect(Collectors.joining(" AND "));
                fetchAllSqlBuilder.append(" WHERE ").append(conditionsJoined);
                countSqlBuilder.append(" WHERE ").append(conditionsJoined);
            }
        });
        fetchAllSqlBuilder.append(GenerateSqlUtil.SPACE).append(GenerateSqlUtil.buildGroupBy(COLUMN_GROUP));

        fetchAllSqlBuilder.append(GenerateSqlUtil.SPACE).append(GenerateSqlUtil.buildOrderBy(pageable.getSort()));

        Query fetchAllQuery = entityManager.createNativeQuery(fetchAllSqlBuilder.toString(),
                DISCOUNT_PROJECTIONS_MAPPING);

        if (pageable.isPaged()) {
            fetchAllQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
        }

        Query countQuery = entityManager.createNativeQuery(countSqlBuilder.toString());

        params.forEach((k, v) -> {
            fetchAllQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        LOG.info("Diccount sql builded : {}", fetchAllSqlBuilder);
        LOG.info("Diccount sql builded : {}", countSqlBuilder);

        final List<DiscountProjections> discounts = fetchAllQuery.getResultList();
        final int totalElements = (int) countQuery.getSingleResult();
        return new PageImpl<>(discounts, pageable, totalElements);
    }

    @Transactional
    @Override
    public Discount create(DiscountDto discountDto) {
        Discount discount = Optional.of(discountDto)
            .map(discountMapper::mapping)
            .get();
        entityManager.persist(discount);
        entityManager.flush();

        final long discountId = discount.getId();

        LOG.info("Discount after persisted : {} and id = {}", discount, discountId);

        this.updateProductsDiscount(discountId, discountDto.getProductIds());
        return discount;
    }

    @Transactional
    @Override
    public Discount update(DiscountDto discountDto) {
        Discount discount = Optional.of(discountDto)
            .map(discountMapper::mapping)
            .get();
        entityManager.merge(discount);
        entityManager.flush();

        final long discountId = discount.getId();

        LOG.info("Discount after merged : {} and id = {}", discount, discountId);
        // remove all old prodcut discount
        productRepository.removeAllDiscount(discountId);
        this.updateProductsDiscount(discountId, discountDto.getProductIds());
        return discount;
    }

    private void updateProductsDiscount(long discountId, Collection<Long> productIds) {
        if(Objects.isNull(productIds)) {
            return;
        }
        LOG.info("ProductIds : {}", productIds);
        productIds.forEach(productId -> {
            this.productRepository.updateDiscount(discountId, productId);  
            LOG.info("Updated product id = {} , discount id = {}", productId, discountId); 
        });
    }



    @Transactional
    @Override
    public void deleteById(Long id) {
        // remove all discount product
        this.productRepository.removeAllDiscount(id);

        // remove discount
        Discount discountFinded = entityManager.find(Discount.class, id);
        this.entityManager.remove(discountFinded);
    }

}