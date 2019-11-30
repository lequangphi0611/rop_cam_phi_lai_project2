package com.electronicssales.models;

import java.util.Set;

import lombok.Data;

@Data
public class CategoryProjections {

    long id;

    String name;

    Set<ProductNameAndIdOnly> products;
    
}