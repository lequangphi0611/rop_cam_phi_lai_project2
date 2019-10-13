package com.electronicssales.repositories.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.electronicssales.entities.Product;
import com.electronicssales.models.responses.FetchProductOption;
import com.electronicssales.models.types.ProductSortType;
import com.electronicssales.models.types.SortType;
import com.electronicssales.repositories.CustomizeProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

public class CustomizeProductRepositoryImpl implements CustomizeProductRepository {

    private static final String PRODUCT_PREFIX = "p";

    private static final String PRODUCT_CATEGORY_PREFIX = "pc";

    private static final String MANUFACTURER_PREFIX = "m";

    @Autowired
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public Page<Product> fetchProductsBy(FetchProductOption option) {
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder builder = buildFetchProductsQueryBy(option, parameters);

        int firstResultIndex = option.getPageable().getPageNumber()  * option.getPageable().getPageSize();

        Query query = entityManager
            .createQuery(builder.toString(), Product.class)
            .setFirstResult(firstResultIndex)
            .setMaxResults(option.getPageable().getPageSize());
            
        parameters.forEach(query::setParameter);
        
        List<Product> products = (List<Product>)query.getResultList();
        return new PageImpl<>(products, option.getPageable(), countAll());
            
    }

    @Transactional
    @Override
    public long countAll() {
        StringBuilder builder = new StringBuilder("SELECT COUNT(")
            .append(PRODUCT_PREFIX)
            .append(") FROM ")
            .append(Product.class.getSimpleName())
            .append(" ")
            .append(PRODUCT_PREFIX);
        
        return entityManager
            .createQuery(builder.toString(), Long.class)
            .getSingleResult();
    }


    private StringBuilder buildFetchProductsQueryBy(FetchProductOption option, Map<String, Object> parameters) {
        StringBuilder builder = createBaseQuery();

        // JOIN
        buildJoin(builder, option);

        Collection<StringBuilder> builders = new ArrayList<>();

        if(!option.getCategoriesId().isEmpty()) {
            builders.add(buildCategoryConditionBy(option.getCategoriesId(), parameters));
        }

        if(!option.getManufacturersId().isEmpty()) {
            builders.add(buildManufacturerConditionBy(option.getManufacturersId(), parameters));
        }

        if(option.getFromPrice() > 0 || option.getToPrice() > 0) {
            builders.add(buildFindProductByPriceRangeCondition(
                option.getFromPrice(), 
                option.getToPrice(), 
                parameters
            ));
        }

        // Where
        builder.append(mergeAllCondition(builders));

        // Order by
        builder.append(buildOrderBy(option.getProductSortType(), option.getSortType()));
        return builder;
    }

    private StringBuilder createBaseQuery() {
        return new StringBuilder("SELECT ")
            .append(PRODUCT_PREFIX)
            .append(" FROM ")
            .append(Product.class.getSimpleName())
            .append(" ")
            .append(PRODUCT_PREFIX);
    }

    private StringBuilder buildJoin(StringBuilder builder, FetchProductOption option) {

        if(!option.getCategoriesId().isEmpty()) {
            builder.append(" LEFT JOIN ")
                .append(PRODUCT_PREFIX)
                .append(".productCategories ")
                .append(PRODUCT_CATEGORY_PREFIX);
        }

        if(!option.getManufacturersId().isEmpty()) {
            builder.append(" JOIN ")
                .append(PRODUCT_PREFIX)
                .append(".manufacturer ")
                .append(MANUFACTURER_PREFIX);
        }

        return builder;
    }

    private StringBuilder buildOrderBy(ProductSortType productSortType, SortType sortType) {
        StringBuilder orderBuilder = new StringBuilder(" ORDER BY");

        Optional
            .ofNullable(productSortType)
            .ifPresent(pSortType -> {
                switch (pSortType) {
                    case PRICE:
                        orderBuilder.append(buildOrderByPrice(sortType));
                        break;
                    case TIME:
                        orderBuilder.append(builderOrderByNew(sortType)); 
                        break;

                    default:
                        break;
                }
                orderBuilder.append(", ");
            });

        orderBuilder
            .append(" ")
            .append(PRODUCT_PREFIX)
            .append(".productName ")
            .append(SortType.ASC);
        return orderBuilder;
    }

    private StringBuilder mergeAllCondition(Collection<StringBuilder> builders) {
        StringBuilder builder  = new StringBuilder();

        if(builders.isEmpty()) {
            return builder;
        }

        List<StringBuilder> builderList = (List<StringBuilder>)builders;
        
        builder.append(" WHERE");
        builder.append(mergeSubConditions(builderList));
        return builder;
    }

    private StringBuilder mergeSubConditions(List<StringBuilder> builders) {
        StringBuilder builder  = new StringBuilder();
        for(int i = 0; i < builders.size(); i++) {
            int index = i;
            Optional.ofNullable(builders.get(i))
                .filter(value -> !StringUtils.isEmpty(value.toString()))
                .ifPresent(value -> {
                    if(index > 0) {
                        builder.append(" AND");
                    }

                    builder
                        .append(" (")
                        .append(value)
                        .append(") ");
                });
        }

        return builder;
    }

    private StringBuilder buildCategoryConditionBy(List<Long> categoriesId, Map<String, Object> parameters) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < categoriesId.size(); i++) {
            if(i > 0) {
                builder.append(" OR");
            }
            String categoryParamName = new StringBuilder("categoryId").append(i).toString();
            builder.append(" ")
                .append(PRODUCT_CATEGORY_PREFIX)
                .append(".category.id = :")
                .append(categoryParamName);
            parameters.put(categoryParamName, categoriesId.get(i));
        }
        return builder;
    }

    private StringBuilder buildManufacturerConditionBy(List<Long> manufacturersId, Map<String, Object> parameters) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < manufacturersId.size(); i++) {
            if(i > 0) {
                builder.append(" OR");
            }

            String manufacturerParamString = new StringBuilder("manufacturerId").append(i).toString();
            builder.append(" ").append(MANUFACTURER_PREFIX).append(".id = :").append(manufacturerParamString);
            parameters.put(manufacturerParamString, manufacturersId.get(i));
        }
        return builder;
    }

    private StringBuilder buildFindProductByPriceRangeCondition(long fromPrice, long toPrice, Map<String, Object> parameters) {
        StringBuilder builder = new StringBuilder();
        String[] rangePriceParamsStr = {"fromPrice", "toPrice"};

        List<StringBuilder> builders = new ArrayList<>();
        if(fromPrice > 0) {
            StringBuilder fromPriceQuery = new StringBuilder(" ")
                .append(PRODUCT_PREFIX)
                .append(".price >= :")
                .append(rangePriceParamsStr[0]);
            builders.add(fromPriceQuery);
            parameters.put(rangePriceParamsStr[0], fromPrice);
        }

        if(toPrice > 0) {
            StringBuilder toPriceQuery = new StringBuilder(" ")
                .append(PRODUCT_PREFIX)
                .append(".price <= :")
                .append(rangePriceParamsStr[1]);
            builders.add(toPriceQuery);
            parameters.put(rangePriceParamsStr[1], toPrice);
        }
        builder.append(mergeSubConditions(builders));

        return builder;
    }

    private StringBuilder buildOrderByPrice(SortType sortType) {
        return new StringBuilder(" ")
            .append(PRODUCT_PREFIX)
            .append(".price ")
            .append(sortType);
    }

    private StringBuilder builderOrderByNew(SortType sortType) {
        return new StringBuilder(" ")
            .append(PRODUCT_PREFIX)
            .append(".createdTime ")
            .append(sortType)
            .append(", ")
            .append(PRODUCT_PREFIX)
            .append(".updatedTime ")
            .append(sortType);
    }

    
}