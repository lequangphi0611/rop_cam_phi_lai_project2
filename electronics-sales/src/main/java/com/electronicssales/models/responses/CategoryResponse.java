package com.electronicssales.models.responses;

import java.util.List;

import lombok.Data;

@Data
public class CategoryResponse {

    long id;
    
    String categoryName;

    Long parentId;

    List<CategoryResponse> childrens;

    private int productCount;

}