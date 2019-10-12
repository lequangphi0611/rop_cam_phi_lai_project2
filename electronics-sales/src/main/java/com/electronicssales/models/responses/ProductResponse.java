package com.electronicssales.models.responses;

import java.util.Collection;
import java.util.Date;

import lombok.Data;

@Data
public class ProductResponse {

    private long id;

    private String productName;

    private long imageId;

    private long price;

    private int quantity;

    private Collection<String> categoryName;

    private String manufacturerName;
    
} 