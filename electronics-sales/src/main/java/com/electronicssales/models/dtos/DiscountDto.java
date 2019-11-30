package com.electronicssales.models.dtos;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountDto {

    private long id;

    private long discountValue;

    private String discountType;

    Collection<Long> productIds;
    
}