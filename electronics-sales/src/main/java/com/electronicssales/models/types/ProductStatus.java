package com.electronicssales.models.types;

import lombok.Getter;

public enum ProductStatus {

    SELLABLE(0),

    UNSELLABLE(1);

    @Getter
    private final int value;

    private ProductStatus(int value) {
        this.value = value;
    }

}