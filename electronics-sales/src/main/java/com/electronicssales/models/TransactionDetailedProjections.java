package com.electronicssales.models;

import lombok.Value;

@Value
public class TransactionDetailedProjections {

    Long id;

    byte[] image;

    String productName;

    long price;

    int quantity;

    String discountType;

    Long discountValue;

    public Long getTotal() {
        return this.price * this.quantity;
    }
    
}