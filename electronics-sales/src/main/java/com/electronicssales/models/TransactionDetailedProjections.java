package com.electronicssales.models;

import lombok.Value;

@Value
public class TransactionDetailedProjections implements TransactionDetailedCalculator {

    Long id;

    byte[] image;

    String productName;

    long price;

    int quantity;

    String discountType;

    Long discountValue;
    
}