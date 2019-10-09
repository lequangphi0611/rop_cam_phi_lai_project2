package com.electronicssales.models.responses;

import java.util.Collection;

import lombok.Data;

@Data
public class CategoryResponse {

    private long id;

    private String categoryName;

    private long parentId;

    private int productCount;

    Collection<CategoryResponse> childrens;

    
}