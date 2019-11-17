package com.electronicssales.models.types;

import java.util.Optional;
import java.util.stream.Stream;

public enum CategoryFetchType {

    FULL,

    PARENT_ONLY,

    CHILDREN_ONLY;

    public static Optional<CategoryFetchType> of(String str) {
        return Optional.of(str).flatMap(
                s -> Stream.of(CategoryFetchType.values())
                    .filter(v -> v.toString().equalsIgnoreCase(s))
                    .findFirst()
        );
    }

}