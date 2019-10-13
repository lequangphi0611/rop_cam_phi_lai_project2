package com.electronicssales.models.types;

import java.util.stream.Stream;

public enum SortType {

    ASC,

    DESC;

    public static SortType of(String arg) {
        return Stream.of(SortType.values())
            .filter(value -> value.toString().equalsIgnoreCase(arg))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
    
}