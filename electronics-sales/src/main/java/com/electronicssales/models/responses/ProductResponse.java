package com.electronicssales.models.responses;

import java.util.Date;

import lombok.Data;

@Data
public class ProductResponse {

    private long id;

    private String productName;

    private long price;

    private int quantity;

    private boolean salable;

    private Date createTime;

    private Date updatedTime;

    private String categoryName;
    
} 