package com.electronicssales.models.types;

import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

public enum Rating {

    VERY_BAD(1),

    BAD(2),

    OKAY(3),

    GOOD(4),

    GREAT(5);

    @Getter 
    @Setter
    private int value;    

    Rating(int value) {
        this.value = value;
    }

    public static Rating of(int value) {
        return Stream
            .of(Rating.values())
            .filter(v -> v.value == value)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

}