package com.electronicssales.repositories.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.electronicssales.models.TransactionFetchOption;
import com.electronicssales.models.TransactionProjections;
import com.electronicssales.repositories.CustomizeTransactionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

public class CustomizeTransactionRepositoryImpl implements CustomizeTransactionRepository {

    private static final String TRANSACTION_PROJECTIONS_MAPPING = "TransactionProjectionsMapping";

    private static final String BASE_FETCH_ALL_QUERY = "SELECT t.id as id, t.created_time as createdTime, "
            + " CONCAT(u.lastname, ' ', u.firstname) as fullname, u.email as email, u.phone_number as phoneNumber,"
            + " u.address as address, SUM(ts.price * quantity) as subTotal, SUM((CASE ts.discount_type"
            + " WHEN 'PERCENT' THEN (ts.price * ts.discount_value / 100) WHEN 'AMOUNT' THEN ts.discount_value"
            + " ELSE 0 END)) as discountTotal, SUM(ts.price - (CASE ts.discount_type WHEN 'PERCENT'"
            + " THEN (ts.price * ts.discount_value / 100) WHEN 'AMOUNT' THEN ts.discount_value ELSE 0"
            + " END) * ts.quantity) as sumTotal FROM transactions t INNER JOIN user_infos u"
            + " ON t.customer_id = u.id INNER JOIN transaction_detaileds ts ON t.id = ts.transaction_id";

    private static final String GROUP_FETCH_ALL_QUERY = " GROUP BY t.id, t.created_time, u.lastname, "
            + " u.firstname, u.email, u.address, u.phone_number";

    private static final String BASE_COUNT_QUERY = "SELECT COUNT(t.id) FROM transactions t";

    private static final int DEFAULT_PAGE_INDEX = 0;

    private static final int DEFAULT_PAGE_SIZE = 10;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "created_time");

    private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

    private final EntityManager entityManager;

    public CustomizeTransactionRepositoryImpl(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public Page<TransactionProjections> fetchAll(TransactionFetchOption option, Pageable pageable) {
        pageable = pageable == null || pageable.isUnpaged() ? DEFAULT_PAGEABLE : pageable;
        // init base sql
        StringBuilder fetchQueryBuilder = new StringBuilder(BASE_FETCH_ALL_QUERY);
        // init base count sql
        StringBuilder countQueryBuilder = new StringBuilder(BASE_COUNT_QUERY);

        //init params
        Map<String, Object> params = new HashMap<>();

        // build condition
        List<String> conditions = new ArrayList<>();
        if(option != null) {
            Optional<Date> fromDateOptional = Optional.ofNullable(option.getFromDate());
            Optional<Date> toDateOptional = Optional.ofNullable(option.getToDate());
            
            fromDateOptional.ifPresent(value -> {
                conditions.add(" CAST(created_time as DATE) >= :fromDate ");
                params.put("fromDate", value);
            });
            toDateOptional.ifPresent(value -> {
                conditions.add(" CAST(created_time as DATE) <= :toDate ");
                params.put("toDate", value);
            });  
        }
        
        if(!params.isEmpty()) {
           StringBuilder conditionsBuilder = new StringBuilder(" WHERE ");
           conditionsBuilder.append(String.join(" AND ", conditions));
           fetchQueryBuilder.append(conditionsBuilder);
           countQueryBuilder.append(conditionsBuilder);
        }

        // append group sql
        fetchQueryBuilder.append(GROUP_FETCH_ALL_QUERY);

        // init order by
        StringBuilder sortBuilder = new StringBuilder(" ORDER BY ");
        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : DEFAULT_SORT;
        List<StringBuilder> orders = new ArrayList<>();
        sort.stream().forEach(order -> {
            StringBuilder orderBuilder = new StringBuilder(" ");
            orderBuilder.append(order.getProperty())
                .append(" ")
                .append(order.getDirection().name())
                .append(" ");
            orders.add(orderBuilder);
        });

        sortBuilder.append(String.join(", ", orders));

        fetchQueryBuilder.append(sortBuilder);

        // execute query

        Query fetchAllQuery = entityManager
            .createNativeQuery(fetchQueryBuilder.toString(), TRANSACTION_PROJECTIONS_MAPPING)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize());

        Query countQuery = entityManager.createNativeQuery(countQueryBuilder.toString());


        params.forEach(fetchAllQuery::setParameter);
        params.forEach(countQuery::setParameter);

        final List<TransactionProjections> transactions = fetchAllQuery.getResultList();
        final int maxSize = (Integer) countQuery.getSingleResult();
        return new PageImpl<TransactionProjections>(transactions, pageable, maxSize);
    }

}