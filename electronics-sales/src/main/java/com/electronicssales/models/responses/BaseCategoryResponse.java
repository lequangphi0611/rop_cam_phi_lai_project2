package com.electronicssales.models.responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseCategoryResponse {

    Long id;
    
    String categoryName;

    Long parentId;

    int productCount;


}