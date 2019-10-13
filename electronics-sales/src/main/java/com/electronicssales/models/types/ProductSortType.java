package com.electronicssales.models.types;

import java.util.stream.Stream;

public enum ProductSortType {

    PRICE,

    TIME,

    REVIEWS;

    public static ProductSortType of(String arg) {
        return Stream.of(ProductSortType.values())
            .filter(value -> value.toString().equalsIgnoreCase(arg))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
    
}