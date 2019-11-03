package com.electronicssales.models.types;

import java.util.stream.Stream;

import lombok.Getter;

public enum Rating {

    VERY_BAD(Rating.MIN_RATING),

    BAD(1),

    OKAY(2),

    GOOD(3),

    GREAT(Rating.MAX_RATING);

    private static final int MIN_RATING = 0;

    private static final int MAX_RATING = 4;

    @Getter
    private final int value;

    Rating(int value) {
        this.value = value;
    }

    public static Rating of(int value) {
        return Stream.of(Rating.values())
            .filter(v -> v.value == value)
            .findFirst()
            .orElseGet(() -> 
                value < MIN_RATING ? Rating.VERY_BAD : Rating.GREAT
            );
    }

}