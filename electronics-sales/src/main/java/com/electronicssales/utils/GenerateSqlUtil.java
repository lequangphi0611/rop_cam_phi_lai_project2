package com.electronicssales.utils;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

public class GenerateSqlUtil {

    public static final String GROUP_BY_KEYWORD = "GROUP BY";

    public static final String ORDER_BY_KEYWORD = "ORDER BY";

    public static final String WHERE_KEYWORD = "WHERE";

    public static final String SPACE = " ";

    public static final String COMMA_DELIMITER = ",";

    public static StringBuilder buildGroupBy(String... column) {
        Objects.requireNonNull(column);
        return new StringBuilder(GROUP_BY_KEYWORD)
            .append(SPACE)
            .append(String.join(COMMA_DELIMITER, column));
    }

    public static StringBuilder buildOrderBy(Sort sort) {
        Objects.requireNonNull(sort);
        if(sort.isUnsorted()) {
            return new StringBuilder();
        }

        String orders = sort.get()
            .map(GenerateSqlUtil::mapOrder)
            .collect(Collectors.joining(COMMA_DELIMITER));
        return new StringBuilder(ORDER_BY_KEYWORD)
            .append(SPACE)
            .append(orders);
    }

    public static StringBuilder mapOrder(Sort.Order order) {
        return new StringBuilder(order.getProperty())
            .append(SPACE)
            .append(order.getDirection().name());
    }
    
}