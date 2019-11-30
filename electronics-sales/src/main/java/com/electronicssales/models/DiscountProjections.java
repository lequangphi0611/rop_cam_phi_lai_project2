package com.electronicssales.models;

import java.util.Date;

import lombok.Value;

@Value
public class DiscountProjections {

    Long id;

    Date statedTime;

    String discountType;

    long discountValue;

    int productCount;
    
}