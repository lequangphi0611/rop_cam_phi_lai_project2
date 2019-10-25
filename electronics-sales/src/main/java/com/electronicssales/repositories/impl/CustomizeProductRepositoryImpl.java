package com.electronicssales.repositories.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.electronicssales.entities.Product;
import com.electronicssales.entities.ProductCategory;
import com.electronicssales.entities.ProductImage;
import com.electronicssales.models.responses.FetchProductOption;
import com.electronicssales.models.types.DiscountType;
import com.electronicssales.models.types.FetchProductType;
import com.electronicssales.models.types.ProductSortType;
import com.electronicssales.models.types.SortType;
import com.electronicssales.repositories.CustomizeProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

public class CustomizeProductRepositoryImpl implements CustomizeProductRepository {

    private static final String PRODUCT_PREFIX = "p";

    private static final String PRODUCT_CATEGORY_PREFIX = "pc";

    private static final String MANUFACTURER_PREFIX = "m";

    private static final String REVIEWS_PREFIX = "rv";

    private static final String DISCOUNT_PREFIX = "dc";

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizeProductRepositoryImpl.class);
    
    @Autowired
    private EntityManager entityManager;

    private static final String[] PRODUCT_COLUMNS_STRING = {
        "id",
        "productName",
        "price",
        "quantity",
        "salable",
        "createdTime",
        "updatedTime",
        "manufacturer",
        "discount"
    };

    private static final String[] DISCOUNT_COLUMNS_STRING = {
        "id",
        "discountValue",
        "startedTime",
        "discountType"
    };

    private static final Map<String, String> PRODUCT_COLUMNS = new HashMap<String, String>() {

        private static final long serialVersionUID = 1L;

        {
            Stream.of(PRODUCT_COLUMNS_STRING)
                .forEach(columnStr -> 
                    put(columnStr, PRODUCT_PREFIX.concat(".").concat(columnStr))
                );
        }
    };

    private static final Map<String, String> DISCOUNT_COLUMNS = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            Stream.of(DISCOUNT_COLUMNS_STRING)
                .forEach(columnStr -> 
                    put(columnStr, DISCOUNT_PREFIX.concat(".").concat(columnStr))
                );
        }
    };

    private static final String  FIND_CATEGORY_IDS_BY_PRODUCT_ID_QUERY = new StringBuilder("SELECT ")
            .append(PRODUCT_CATEGORY_PREFIX)
            .append(".category.id FROM ")
            .append(ProductCategory.class.getSimpleName().concat(" "))
            .append(PRODUCT_CATEGORY_PREFIX)
            .append(" WHERE ")
            .append(PRODUCT_CATEGORY_PREFIX)
            .append(".product.id = :productId")
            .toString();

    private static final String FIND_IMAGE_IDS_BY_PRODUCT_ID_QUERY = new StringBuilder("SELECT pi.image.id FROM ")
            .append(ProductImage.class.getSimpleName())
            .append(" pi WHERE pi.product.id = :productId")
            .toString();

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public Page<Product> fetchProductsBy(FetchProductOption option) {
        Map<String, Object> parameters = new HashMap<>();
        String sqlbuilded = buildFetchProductsQueryBy(option, parameters).toString();
        LOGGER.info("My JPQL Builded : {}", sqlbuilded);

        int firstResultIndex = option.getPageable().getPageNumber()  * option.getPageable().getPageSize();

        Query query = entityManager
            .createQuery(sqlbuilded.toString(), Product.class)
            .setFirstResult(firstResultIndex)
            .setMaxResults(option.getPageable().getPageSize());
            
        parameters.forEach(query::setParameter);
        
        List<Product> products = (List<Product>)query.getResultList();
        return new PageImpl<>(products, option.getPageable(), products.size());
            
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

    @Transactional
    @Override
    public List<Long> findCategoryIdsByProductId(long productId) {
        return entityManager.createQuery(FIND_CATEGORY_IDS_BY_PRODUCT_ID_QUERY, Long.class)
            .setParameter("productId", productId)
            .getResultList();
    }

    @Override
    public List<Long> findImageIdsByProductId(long productId) {
        return entityManager
            .createQuery(FIND_IMAGE_IDS_BY_PRODUCT_ID_QUERY, Long.class)
            .setParameter("productId", productId)
            .getResultList();
    }


    private StringBuilder buildFetchProductsQueryBy(FetchProductOption option, Map<String, Object> parameters) {
        StringBuilder builder = initQuery();

        // JOIN
        buildJoin(builder, option);

        // Where
        buildConditionsQuery(builder, option, parameters);

        // Group by
        boolean group = (option.getProductSortType() != null 
             && option.getProductSortType() == ProductSortType.REVIEWS)
            || (option.isFetchDiscount()
               && option.getFetchProductType() == FetchProductType.DISCOUNT);
        if(group) {
            groupBy(builder);
        }

        // Order by
        builder.append(buildOrderBy(option.getProductSortType(), option.getSortType()));
        return builder;
    }

    private StringBuilder buildConditionsQuery(StringBuilder builder,FetchProductOption option, Map<String, Object> parameters) {

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

        Optional.ofNullable(option.getSearchKey())
            .ifPresent(searchKey -> {
                builders.add(buildQueryBySearchKey(searchKey, parameters));
            });
        
        if(option.getFetchProductType() != FetchProductType.ALL
            && option.getFetchProductType() != FetchProductType.DISCOUNT) {
            builders.add(buildConditionsByFetchType(option.getFetchProductType()));
        }

        builder.append(mergeAllCondition((List<StringBuilder>)builders));

        return builder;
    }

    private StringBuilder initQuery() {
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
                .append(PRODUCT_COLUMNS
                    .get("manufacturer")
                    .concat(" ")
                    .concat(MANUFACTURER_PREFIX)
                );
        }

        if(option.getProductSortType() != null 
            && option.getProductSortType() == ProductSortType.REVIEWS) {
                builder.append(" LEFT JOIN ")
                    .append(PRODUCT_PREFIX)
                    .append(".reviews ")
                    .append(REVIEWS_PREFIX);
        }

        if(option.isFetchDiscount()) {
            if(option.getFetchProductType() != FetchProductType.DISCOUNT) {
            builder.append(" LEFT");
            }

            builder
                .append(" JOIN FETCH ")
                .append(PRODUCT_PREFIX)
                .append(".discount ")
                .append(DISCOUNT_PREFIX);
        }

        return builder;
    }

    private StringBuilder buildConditionsByFetchType(FetchProductType fetchProductType) {
        switch (fetchProductType) {
            case SELLING:
                return buildConditionsByFetchTypeEqualsSelling();
        
            case SALABLE:
                return buildConditionsByFetchTypeEqualsSalable();
            
            default:
                return buildConditionsByFetchTypeEqualsUnselling();
        }
    }

    private StringBuilder buildConditionsByFetchTypeEqualsSalable() {
        return new StringBuilder(PRODUCT_COLUMNS.get("salable"))
            .append(" = 1");
    }

    private StringBuilder buildConditionsByFetchTypeEqualsSelling() {
        return new StringBuilder(buildConditionsByFetchTypeEqualsSalable())
            .append(" AND ")
            .append(PRODUCT_COLUMNS.get("quantity"))
            .append(" > 0");
    }

    private StringBuilder buildConditionsByFetchTypeEqualsUnselling() {
        return new StringBuilder(PRODUCT_COLUMNS.get("salable"))
            .append(" = 0");
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
                        orderBuilder.append(buildOrderByNew(sortType)); 
                        break;
                    
                    case DISCOUNT:
                        orderBuilder.append(builderOrderByDiscount(sortType));
                        break;

                    default:
                        orderBuilder.append(buildOrderByReviews(sortType));
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

    private StringBuilder mergeAllCondition(List<StringBuilder> builders) {
        StringBuilder builder  = new StringBuilder();

        if(builders.isEmpty()) {
            return builder;
        }
        
        builder.append(" WHERE");
        builder.append(mergeSubConditions(builders));
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
                        .append(")");
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

    private StringBuilder buildQueryBySearchKey(String searchKey, Map<String, Object> parameters) {
        final String searchKeyParamStr = "searchKey";
        StringBuilder builder = new StringBuilder(" ")
            .append(PRODUCT_COLUMNS.get("productName"))
            .append(" LIKE :")
            .append(searchKeyParamStr);
        parameters.put(
            searchKeyParamStr, 
            new StringBuilder("%")
                .append(searchKey)
                .append("%")
                .toString()
        );
        return builder;
    }

    private StringBuilder buildFindProductByPriceRangeCondition(long fromPrice, long toPrice, Map<String, Object> parameters) {
        StringBuilder builder = new StringBuilder();
        String[] rangePriceParamsStr = {"fromPrice", "toPrice"};

        List<StringBuilder> builders = new ArrayList<>();
        if(fromPrice > 0) {
            StringBuilder fromPriceQuery = new StringBuilder(" ")
                .append(PRODUCT_COLUMNS.get("price").concat(" >= :"))
                .append(rangePriceParamsStr[0]);
            builders.add(fromPriceQuery);
            parameters.put(rangePriceParamsStr[0], fromPrice);
        }

        if(toPrice > 0) {
            StringBuilder toPriceQuery = new StringBuilder(" ")
                .append(PRODUCT_COLUMNS.get("price").concat(" <= :"))
                .append(rangePriceParamsStr[1]);
            builders.add(toPriceQuery);
            parameters.put(rangePriceParamsStr[1], toPrice);
        }
        builder.append(mergeSubConditions(builders));

        return builder;
    }

    private StringBuilder buildOrderByPrice(SortType sortType) {
        return new StringBuilder(" ")
            .append(PRODUCT_COLUMNS.get("price").concat(" "))
            .append(sortType);
    }

    private StringBuilder buildOrderByNew(SortType sortType) {
        return new StringBuilder(" ")
            .append(PRODUCT_COLUMNS.get("createdTime"))
            .append(" ")
            .append(sortType)
            .append(", ")
            .append(PRODUCT_COLUMNS.get("updatedTime"))
            .append(" ")
            .append(sortType);
    }

    private StringBuilder buildOrderByReviews(SortType sortType) {
        return new StringBuilder(" SUM(")
            .append(REVIEWS_PREFIX)
            .append(".rating) ")
            .append(sortType);
    }

    private StringBuilder builderOrderByDiscount(SortType sortType) {
        return new StringBuilder(" CASE ")
            .append(DISCOUNT_COLUMNS.get(DISCOUNT_COLUMNS_STRING[3]))
            .append(" WHEN '")
            .append(DiscountType.PERCENT)
            .append("' THEN (")
            .append(PRODUCT_COLUMNS.get(PRODUCT_COLUMNS_STRING[2]))
            .append(" * ")
            .append(DISCOUNT_COLUMNS.get(DISCOUNT_COLUMNS_STRING[1]))
            .append(" / 100 )")
            .append(" ELSE ")
            .append(DISCOUNT_COLUMNS.get(DISCOUNT_COLUMNS_STRING[1]))
            .append(" END ")
            .append(sortType);
    }

    private void groupBy(StringBuilder builder) {
        builder.append(" GROUP BY ");

        List<String> columns = new ArrayList<>();

        PRODUCT_COLUMNS
            .entrySet()
            .stream()
            .map(value -> value.getValue())
            .forEach(columns::add);
        
        DISCOUNT_COLUMNS
            .entrySet()
            .stream()
            .map(value -> value.getValue())
            .forEach(columns::add);
        
        for(int i = 0; i < columns.size(); i++) {
            if(i > 0) {
                builder.append(", ");
            }
            builder.append(columns.get(i));
        }
    }

    
}