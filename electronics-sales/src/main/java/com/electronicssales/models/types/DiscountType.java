package com.electronicssales.models.types;

import java.util.Optional;
import java.util.stream.Stream;

public enum DiscountType {

    PERCENT,

    AMOUNT;

    public static Optional<DiscountType> of(String arg) {
        return Stream.of(DiscountType.values())
            .filter(discountType -> discountType.toString().equalsIgnoreCase(arg))
            .findFirst();
    }

}