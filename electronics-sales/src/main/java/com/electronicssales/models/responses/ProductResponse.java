package com.electronicssales.models.responses;

import java.util.Collection;

import com.electronicssales.models.responses.DiscountResponse;

import lombok.Data;

@Data
public class ProductResponse {

    private long id;

    private String productName;

    private Collection<Long> imageIds;

    private long price;

    private int quantity;

    private Collection<Long> categoryIds;

    private Long manufacturerId;

    private DiscountResponse discount;
    
} 