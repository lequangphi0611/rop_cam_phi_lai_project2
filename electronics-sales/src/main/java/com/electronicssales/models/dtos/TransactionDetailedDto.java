package com.electronicssales.models.dtos;

import com.electronicssales.models.types.DiscountType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionDetailedDto {

    long productId;

    int quantity;

    long price;

    DiscountType discountType;

    long discountValue;
    
}