package com.electronicssales.repositories.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.electronicssales.models.DiscountFetchOption;
import com.electronicssales.models.DiscountProjections;
import com.electronicssales.repositories.CustomizeDiscountRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public class CustomizeDiscountRepositoryImpl implements CustomizeDiscountRepository {

    private static final String BASE_FETCH_ALL_DISCOUNT = "SELECT d.id AS id,"
        + " d.discount_type AS discountType,"
        + " d.discount_value as discountValue,"
        + " d.started_time as startedTime,"
        + " COUNT(p.id) as productCount"
        + " FROM discounts d LEFT JOIN products p ON d.id = p.discount_id"
        + " GROUP BY d.id, d.discount_type, d.discount_value, d.started_time";

    private static final String BASE_COUNT_DISCOUNT_QUERY = "SELECT COUNT(d.id) FROM discounts d";

    private static final String DISCOUNT_PROJECTIONS_MAPPING = "DiscountProjectionsMapping";

    private final EntityManager entityManager;

    public CustomizeDiscountRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public Page<DiscountProjections> fetchDiscounts(DiscountFetchOption option, Pageable pageable) {
        StringBuilder fetchAllSqlBuilder = new StringBuilder(BASE_FETCH_ALL_DISCOUNT);
        StringBuilder countSqlBuilder = new StringBuilder(BASE_COUNT_DISCOUNT_QUERY);

        Query fetchAllQuery = entityManager.createNativeQuery(fetchAllSqlBuilder.toString(), DISCOUNT_PROJECTIONS_MAPPING);

        Query countQuery = entityManager.createNativeQuery(countSqlBuilder.toString());

        final List<DiscountProjections> discounts = fetchAllQuery.getResultList();
        final int totalElements = (int) countQuery.getSingleResult();
        return new PageImpl<>(discounts, pageable, totalElements);
    }

}