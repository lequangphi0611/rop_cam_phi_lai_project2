package com.electronicssales.models.types;

import lombok.Getter;
import lombok.Setter;

public enum Rating {

    VERY_BAD(0),

    BAD(1),

    OKAY(2),

    GOOD(3),

    GREAT(4);

    @Getter 
    @Setter
    private int value;    

    Rating(int value) {
        this.value = value;
    }

}