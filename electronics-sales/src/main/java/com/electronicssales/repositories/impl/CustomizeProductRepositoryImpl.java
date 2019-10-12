package com.electronicssales.repositories.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.electronicssales.entities.Product;
import com.electronicssales.models.responses.FetchProductOption;
import com.electronicssales.repositories.CustomizeProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CustomizeProductRepositoryImpl implements CustomizeProductRepository {

    @Autowired
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public Collection<Product> fetchProductsBy(FetchProductOption option) {
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder builder = buildFetchProductsQueryBy(option, parameters);
        Query query = entityManager
            .createQuery(builder.toString(), Product.class);
            
        parameters
            .entrySet()
            .stream()
            .forEach(param -> {
                query.setParameter(param.getKey(), param.getValue());
            }); 
        return (List<Product>)query.getResultList();
            
    }

    private StringBuilder buildFetchProductsQueryBy(FetchProductOption option, Map<String, Object> parameters) {
        StringBuilder builder = new StringBuilder("SELECT p FROM Product p");

        buildJoin(builder, option);

        Collection<StringBuilder> builders = new ArrayList<>();

        if(!option.getCategoriesId().isEmpty()) {
            builders.add(buildCategoryConditionBy(option.getCategoriesId(), parameters));
        }

        if(!option.getManufacturersId().isEmpty()) {
            builders.add(buildManufacturerConditionBy(option.getManufacturersId(), parameters));
        }

        builder.append(mergeAllCondition(builders));

        return builder;
    }

    private StringBuilder buildJoin(StringBuilder builder, FetchProductOption option) {

        if(!option.getCategoriesId().isEmpty()) {
            builder
            .append(" LEFT JOIN p.productCategories pc");
        }

        if(!option.getManufacturersId().isEmpty()) {
            builder
                .append(" JOIN p.manufacturer m");
        }

        return builder;
    }

    private StringBuilder mergeAllCondition(Collection<StringBuilder> builders) {
        StringBuilder builder  = new StringBuilder();
        List<StringBuilder> builderList = (List<StringBuilder>)builders;
        if(!builders.isEmpty()) {
            builder.append(" WHERE");

            for(int i = 0; i < builderList.size(); i++) {
                if(i > 0) {
                    builder.append(" AND");
                }

                builder
                    .append(" (")
                    .append(builderList.get(i))
                    .append(")");
            }
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
            builder.append(" pc.category.id = :")
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
            builder.append(" m.id = :").append(manufacturerParamString);
            parameters.put(manufacturerParamString, manufacturersId.get(i));
        }
        return builder;
    }

    
}