package com.electronicssales.models.types;

import java.util.stream.Stream;

public enum FetchProductType {

    SELLING,

    SALABLE,

    ALL,

    DISCOUNT,

    UNSELLING;

    public static FetchProductType of(String arg) {
        return Stream.of(FetchProductType.values())
            .filter(fetchType -> fetchType.toString().equalsIgnoreCase(arg))
            .findFirst()
            .orElse(FetchProductType.ALL);
    }
    
}