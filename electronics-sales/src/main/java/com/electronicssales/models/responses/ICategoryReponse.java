package com.electronicssales.models.responses;

public interface ICategoryReponse {

    long getId();

    String getCategoryName();

    Long getParentId();

    int getProductCount();
    
}